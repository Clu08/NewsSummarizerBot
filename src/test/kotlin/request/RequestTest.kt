package prod.prog.request

import common.UnitTest
import io.github.vjames19.futures.jdk8.Future
import io.github.vjames19.futures.jdk8.recoverWith
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.future.await
import prod.prog.actionProperties.Context
import prod.prog.request.resultHandler.ErrorHandler
import prod.prog.request.resultHandler.ResultHandler
import prod.prog.request.source.Source
import prod.prog.request.source.invoke
import prod.prog.request.transformer.Transformer
import prod.prog.service.supervisor.Supervisor
import prod.prog.service.supervisor.solver.Solver
import prod.prog.service.supervisor.solver.requestSolver.RequestContextSolver

class RequestTest : StringSpec({
    tags(UnitTest)
    isolationMode = IsolationMode.InstancePerTest

    val before = mockk<Solver<Context>>()
    val after = mockk<Solver<Context>>()
    val initContext = mockk<RequestContextSolver>()
    val supervisor = mockk<Supervisor>()

    val source = mockk<Source<Int>>()
    val transformer = mockk<Transformer<Int, Int>>()
    val resultHandler = mockk<ResultHandler<Int>>()
    val errorHandler = mockk<ErrorHandler>()
    val context = RequestContext()
    val request = Request(source, transformer, resultHandler, errorHandler, context)

    val init = 0
    val correct = 1
    val wrong = 2
    val error = Error("ERROR")

    every { supervisor.before } returns before
    every { supervisor.after } returns after
    every { supervisor.getInitContext() } returns initContext

    justRun { source.addContext(any()) }
    justRun { transformer.addContext(any()) }
    justRun { resultHandler.addContext(any()) }
    justRun { errorHandler.addContext(any()) }

    every { source.getContext(any()) } returns context.sourceContext
    every { transformer.getContext(any()) } returns context.transformerContext
    every { resultHandler.getContext(any()) } returns context.resultHandlerContext
    every { errorHandler.getContext(any()) } returns context.errorHandlerContext

    every { initContext(any()) } returns context
    every { before(context.sourceContext) } returns context.sourceContext
    every { before(context.transformerContext) } returns context.transformerContext
    every { before(context.resultHandlerContext) } returns context.resultHandlerContext
    every { before(context.errorHandlerContext) } returns context.errorHandlerContext

    every { after(context.sourceContext) } returns context.sourceContext
    every { after(context.transformerContext) } returns context.transformerContext
    every { after(context.resultHandlerContext) } returns context.resultHandlerContext
    every { after(context.errorHandlerContext) } returns context.errorHandlerContext

    every { source() } returns Future{init}
    every { transformer(any<Int>()) } returns correct
    justRun { resultHandler(any<Int>()) }
    justRun { errorHandler(any<Throwable>()) }

    "run returns correct result in success case" {
        val result = request.run(supervisor).recoverWith { Future { wrong } }

        result.await() shouldBe correct
    }

    "run returns correct result on error in source" {
        every { source() } throws error

        val result = request.run(supervisor).recoverWith { Future { wrong } }

        result.await() shouldBe wrong
    }

    "run returns correct result on error in transformer" {
        every { transformer(any<Int>()) } throws error

        val result = request.run(supervisor).recoverWith { Future { wrong } }

        result.await() shouldBe wrong
    }

    "run returns correct result on error in result handler" {
        every { resultHandler(any<Int>()) } throws error

        val result = request.run(supervisor).recoverWith { Future { wrong } }

        result.await() shouldBe correct
    }

    "run returns correct result on error in error handler" {
        every { errorHandler(any<Throwable>()) } throws error

        val result = request.run(supervisor).recoverWith { Future { wrong } }

        result.await() shouldBe correct
    }

    "run uses expected order in success case" {
        request.run(supervisor)

        verifySequence {
            initContext(any())
            before(context.sourceContext)
            after(context.sourceContext)
            before(context.transformerContext)
            after(context.transformerContext)
            before(context.resultHandlerContext)
            after(context.resultHandlerContext)
        }
    }

    "run uses expected order with error in source" {
        every { source() } throws error

        request.run(supervisor)

        verifySequence {
            initContext(any())
            before(context.sourceContext)
            before(context.errorHandlerContext)
            after(context.errorHandlerContext)
        }
    }

    "run uses expected order with error in transformer" {
        every { transformer(any<Int>()) } throws error

        request.run(supervisor)

        verifySequence {
            initContext(any())
            before(context.sourceContext)
            after(context.sourceContext)
            before(context.transformerContext)
            before(context.errorHandlerContext)
            after(context.errorHandlerContext)
        }
    }

    "run uses expected order with error in result handling" {
        every { resultHandler(any<Int>()) } throws error

        request.run(supervisor)

        verifySequence {
            initContext(any())
            before(context.sourceContext)
            after(context.sourceContext)
            before(context.transformerContext)
            after(context.transformerContext)
            before(context.resultHandlerContext)
            before(context.errorHandlerContext)
            after(context.errorHandlerContext)
        }
    }

    "run uses expected order with error in error handling" {
        every { source() } throws error
        every { errorHandler(any<Throwable>()) } throws error

        request.run(supervisor)

        verifySequence {
            initContext(any())
            before(context.sourceContext)
            before(context.errorHandlerContext)
        }
    }

    "run uses expected order with error in result handling and in error handling" {
        every { resultHandler(any<Int>()) } throws error
        every { errorHandler(any<Throwable>()) } throws error

        request.run(supervisor)

        verifySequence {
            initContext(any())
            before(context.sourceContext)
            after(context.sourceContext)
            before(context.transformerContext)
            after(context.transformerContext)
            before(context.resultHandlerContext)
            before(context.errorHandlerContext)
        }
    }
})