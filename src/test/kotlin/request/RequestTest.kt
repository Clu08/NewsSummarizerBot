package request

import io.github.vjames19.futures.jdk8.Future
import io.github.vjames19.futures.jdk8.recoverWith
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifySequence
import org.junit.jupiter.api.extension.ExtendWith
import prod.prog.actionProperties.Action
import prod.prog.actionProperties.ActionContext
import prod.prog.actionProperties.ActionWithContext
import prod.prog.request.Request
import prod.prog.request.RequestContext
import prod.prog.request.resultHandler.ErrorHandler
import prod.prog.request.resultHandler.ResultHandler
import prod.prog.request.source.Source
import prod.prog.request.transformer.Transformer
import prod.prog.service.supervisor.Supervisor
import prod.prog.service.supervisor.solver.Solver
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class RequestTest {
    @MockK
    private lateinit var before: Solver<ActionWithContext<out Action>>

    @MockK
    private lateinit var after: Solver<ActionWithContext<out Action>>

    @MockK
    private lateinit var initContext: Solver<RequestContext>

    @MockK
    private lateinit var source: Source<Int>

    @MockK
    private lateinit var transformer: Transformer<Int, Int>

    @MockK
    private lateinit var resultHandler: ResultHandler<Int>

    @MockK
    private lateinit var errorHandler: ErrorHandler
    private val context = RequestContext()
    private val init = 0
    private val correct = 1
    private val wrong = 2
    private val error = Error("ERROR")

    private lateinit var sourceWithContext: ActionWithContext<Action>
    private lateinit var transformerWithContext: ActionWithContext<Action>
    private lateinit var resultHandlerWithContext: ActionWithContext<Action>
    private lateinit var errorHandlerWithContext: ActionWithContext<Action>

    @BeforeTest
    fun before() {
        sourceWithContext = ActionWithContext(ActionContext.system(), source)
        transformerWithContext = ActionWithContext(ActionContext.system(), transformer)
        resultHandlerWithContext = ActionWithContext(ActionContext.system(), resultHandler)
        errorHandlerWithContext = ActionWithContext(ActionContext.system(), errorHandler)

        every { source.addContext(any()) } returns sourceWithContext
        every { transformer.addContext(any()) } returns transformerWithContext
        every { resultHandler.addContext(any()) } returns resultHandlerWithContext
        every { errorHandler.addContext(any()) } returns errorHandlerWithContext

        every { initContext(any()) } returns context
        every { before(sourceWithContext) } returns sourceWithContext
        every { before(transformerWithContext) } returns transformerWithContext
        every { before(resultHandlerWithContext) } returns resultHandlerWithContext
        every { before(errorHandlerWithContext) } returns errorHandlerWithContext

        every { after(sourceWithContext) } returns sourceWithContext
        every { after(transformerWithContext) } returns transformerWithContext
        every { after(resultHandlerWithContext) } returns resultHandlerWithContext
        every { after(errorHandlerWithContext) } returns errorHandlerWithContext

        every { source.invoke() } returns Future { init }
        every { transformer.invoke(any()) } returns correct
        every { resultHandler.invoke(any()) } returns Unit
        every { errorHandler.invoke(any()) } returns Unit
    }

    @Test
    fun `validate run returns correct result in success case`() {
        val supervisor = Supervisor(before, after, initContext)
        val request = Request(source, transformer, resultHandler, errorHandler, context)

        val result = request.run(supervisor).recoverWith { Future { wrong } }.get()

        assertEquals(result, correct)
    }

    @Test
    fun `validate run returns correct result on error in source`() {
        val supervisor = Supervisor(before, after, initContext)
        val request = Request(source, transformer, resultHandler, errorHandler, context)
        every { source.invoke() } returns Future { throw error }

        val result = request.run(supervisor).recoverWith { Future { wrong } }.get()

        assertEquals(result, wrong)
    }

    @Test
    fun `validate run returns correct result on error in transformer`() {
        val supervisor = Supervisor(before, after, initContext)
        val request = Request(source, transformer, resultHandler, errorHandler, context)
        every { transformer.invoke(any()) } throws error

        val result = request.run(supervisor).recoverWith { Future { wrong } }.get()

        assertEquals(result, wrong)
    }

    @Test
    fun `validate run returns correct result on error in result handler`() {
        val supervisor = Supervisor(before, after, initContext)
        val request = Request(source, transformer, resultHandler, errorHandler, context)
        every { resultHandler.invoke(any()) } throws error

        val result = request.run(supervisor).recoverWith { Future { wrong } }.get()

        assertEquals(result, correct)
    }

    @Test
    fun `validate run returns correct result on error in error handler`() {
        val supervisor = Supervisor(before, after, initContext)
        val request = Request(source, transformer, resultHandler, errorHandler, context)
        every { errorHandler.invoke(any()) } throws error

        val result = request.run(supervisor).recoverWith { Future { wrong } }.get()

        assertEquals(result, correct)
    }

    @Test
    fun `validate run uses expected order in success case`() {
        val supervisor = Supervisor(before, after, initContext)
        val request = Request(source, transformer, resultHandler, errorHandler, context)

        request.run(supervisor)

        verifySequence {
            initContext(any())
            before(sourceWithContext)
            after(sourceWithContext)
            before(transformerWithContext)
            after(transformerWithContext)
            before(resultHandlerWithContext)
            after(resultHandlerWithContext)
        }
    }

    @Test
    fun `validate run uses expected order with error in source`() {
        val supervisor = Supervisor(before, after, initContext)
        val request = Request(source, transformer, resultHandler, errorHandler, context)
        every { source.invoke() } returns Future { throw error }

        request.run(supervisor)

        verifySequence {
            initContext(any())
            before(sourceWithContext)
            before(errorHandlerWithContext)
            after(errorHandlerWithContext)
        }
    }

    @Test
    fun `validate run uses expected order with error in transformer`() {
        val supervisor = Supervisor(before, after, initContext)
        val request = Request(source, transformer, resultHandler, errorHandler, context)
        every { transformer.invoke(any()) } throws error

        request.run(supervisor)

        verifySequence {
            initContext(any())
            before(sourceWithContext)
            after(sourceWithContext)
            before(transformerWithContext)
            before(errorHandlerWithContext)
            after(errorHandlerWithContext)
        }
    }

    @Test
    fun `validate run uses expected order with error in result handling`() {
        val supervisor = Supervisor(before, after, initContext)
        val request = Request(source, transformer, resultHandler, errorHandler, context)
        every { resultHandler.invoke(any()) } throws error

        request.run(supervisor)

        verifySequence {
            initContext(any())
            before(sourceWithContext)
            after(sourceWithContext)
            before(transformerWithContext)
            after(transformerWithContext)
            before(resultHandlerWithContext)
            before(errorHandlerWithContext)
            after(errorHandlerWithContext)
        }
    }

    @Test
    fun `validate run uses expected order with error in error handling`() {
        val supervisor = Supervisor(before, after, initContext)
        val request = Request(source, transformer, resultHandler, errorHandler, context)
        every { source.invoke() } returns Future { throw error }
        every { errorHandler.invoke(any()) } throws error

        request.run(supervisor)

        verifySequence {
            initContext(any())
            before(sourceWithContext)
            before(errorHandlerWithContext)
        }
    }

    @Test
    fun `validate run uses expected order with error in result handling and in error handling`() {
        val supervisor = Supervisor(before, after, initContext)
        val request = Request(source, transformer, resultHandler, errorHandler, context)
        every { resultHandler.invoke(any()) } throws error
        every { errorHandler.invoke(any()) } throws error

        request.run(supervisor)

        verifySequence {
            initContext(any())
            before(sourceWithContext)
            after(sourceWithContext)
            before(transformerWithContext)
            after(transformerWithContext)
            before(resultHandlerWithContext)
            before(errorHandlerWithContext)
        }
    }
}