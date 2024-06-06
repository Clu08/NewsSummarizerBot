package prod.prog.request

import io.github.vjames19.futures.jdk8.map
import io.github.vjames19.futures.jdk8.onComplete
import io.sentry.Sentry
import io.sentry.SpanStatus
import prod.prog.actionProperties.Context
import prod.prog.request.resultHandler.IgnoreErrorHandler
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

        var (_, sourceContext, transformerContext, resultHandlerContext, errorHandlerContext) =
            supervisor.getInitContext()(
                initContext.copy(
                    sourceContext = source.getContext(initContext.sourceContext),
                    transformerContext = transformer.getContext(initContext.transformerContext),
                    resultHandlerContext = resultHandler.getContext(initContext.resultHandlerContext),
                    errorHandlerContext = errorHandler.getContext(initContext.errorHandlerContext),
                )
            )

        fun handleError(throwable: Throwable, change: Context) {
            errorHandlerContext = supervisor.before(errorHandlerContext.add(change))
            errorHandler(throwable)
            supervisor.after(errorHandlerContext)
        }

        var change = Context()
        // todo add test for context propagating
        sourceContext = supervisor.before(sourceContext)

        return source().map {
            change = supervisor.after(sourceContext).diff(sourceContext)
            transformerContext = supervisor.before(transformerContext.add(change))
            transformer(it).also {
                change = supervisor.after(transformerContext).diff(sourceContext)
            }
        }.onComplete(onSuccess = { result ->
            try {
                resultHandlerContext = supervisor.before(resultHandlerContext.add(change))
                resultHandler(result)
                change = supervisor.after(resultHandlerContext).diff(resultHandlerContext)
            } catch (throwable: Throwable) {
                transaction.throwable = throwable
                transaction.status = SpanStatus.INTERNAL_ERROR
                handleError(throwable, change)
            } finally {
                transaction.finish()
            }
        }, onFailure = { throwable ->
            transaction.throwable = throwable
            transaction.status = SpanStatus.INTERNAL_ERROR
            try {
                handleError(throwable, change)
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
            IgnoreErrorHandler(),
            RequestContext()
        )

        fun <T, R> basicTransformerRequest(transformer: Transformer<T, R>) = { value: T ->
            Request(
                ConstantSource(value),
                transformer,
                IgnoreHandler(),
                IgnoreErrorHandler(),
                RequestContext()
            )
        }
    }
}