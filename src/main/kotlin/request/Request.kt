package prod.prog.request

import io.github.vjames19.futures.jdk8.map
import io.github.vjames19.futures.jdk8.onComplete
import io.sentry.Sentry
import io.sentry.SpanStatus
import prod.prog.request.resultHandler.IgnoreHandler
import prod.prog.request.resultHandler.ResultHandler
import prod.prog.request.source.ConstantSource
import prod.prog.request.source.Source
import prod.prog.request.source.invoke
import prod.prog.request.transformer.IdTransformer
import prod.prog.request.transformer.Transformer
import prod.prog.service.supervisor.Supervisor
import java.util.concurrent.CompletableFuture


data class Request<T, R>(
    val source: Source<T>,
    val transformer: Transformer<T, R>,
    val resultHandler: ResultHandler<R>,
    val errorHandler: ResultHandler<Throwable>,
    val initContext: RequestContext,
) {
    fun run(supervisor: Supervisor): CompletableFuture<R> {
        val transaction = Sentry.startTransaction("run()", "Request")
        val x = initContext.copy(
            sourceContext = source.getContext(initContext.sourceContext),
            transformerContext = transformer.getContext(initContext.transformerContext),
            resultHandlerContext = resultHandler.getContext(initContext.resultHandlerContext),
            errorHandlerContext = errorHandler.getContext(initContext.errorHandlerContext),
        )
        val (_, sourceContext, transformerContext, resultHandlerContext, errorHandlerContext) =
            supervisor.getInitContext()(
                x
            )

        fun handleError(throwable: Throwable) {
            supervisor.before(errorHandlerContext)
            errorHandler(throwable)
            supervisor.after(errorHandlerContext)
        }

        supervisor.before(sourceContext)

        return source().map {
            supervisor.after(sourceContext)
            supervisor.before(transformerContext)
            transformer(it).also {
                supervisor.after(transformerContext)
            }
        }.onComplete(onSuccess = { result ->
            try {
                supervisor.before(resultHandlerContext)
                resultHandler(result)
                supervisor.after(resultHandlerContext)
            } catch (throwable: Throwable) {
                transaction.throwable = throwable
                transaction.status = SpanStatus.INTERNAL_ERROR
                handleError(throwable)
            } finally {
                transaction.finish()
            }
        }, onFailure = { throwable ->
            transaction.throwable = throwable
            transaction.status = SpanStatus.INTERNAL_ERROR
            try {
                handleError(throwable)
            } finally {
                transaction.finish()
            }
        })
    }

    fun get(supervisor: Supervisor): R = run(supervisor).get()

    fun addToInitContext(vararg pairs: Pair<String, Any>): Request<T, R> =
        this.copy(
            initContext = initContext.copy(
                requestSpecificContext = initContext.requestSpecificContext
                    .add(*pairs)
            )
        )

    companion object {
        fun <T> basicSourceRequest(source: Source<T>) = Request(
            source,
            IdTransformer(),
            IgnoreHandler(),
            IgnoreHandler(),
            RequestContext()
        )

        fun <T, R> basicTransformerRequest(transformer: Transformer<T, R>) = { value: T ->
            Request(
                ConstantSource(value),
                transformer,
                IgnoreHandler(),
                IgnoreHandler(),
                RequestContext()
            )
        }
    }
}