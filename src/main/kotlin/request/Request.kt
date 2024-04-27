package prod.prog.request

import io.github.vjames19.futures.jdk8.map
import io.github.vjames19.futures.jdk8.onComplete
import prod.prog.request.resultHandler.ResultHandler
import prod.prog.request.source.Source
import prod.prog.request.transformer.Transformer
import prod.prog.service.supervisor.Supervisor
import java.util.concurrent.CompletableFuture

data class Request<T, R>(
    var source: Source<T>,
    var transformer: Transformer<T, R>,
    var resultHandler: ResultHandler<R>,
    var errorHandler: ResultHandler<Throwable>,
    var requestContext: RequestContext,
) {
    fun run(supervisor: Supervisor): CompletableFuture<R> {
        val newContext = supervisor.initContext(requestContext)
        val source = source.addContext(newContext.sourceContext)
        val transformer = transformer.addContext(newContext.transformerContext)
        val resultHandler = resultHandler.addContext(newContext.resultHandlerContext)
        val errorHandler = errorHandler.addContext(newContext.errorHandlerContext)

        supervisor.before(source)
        return source()
            .map {
                supervisor.after(source)
                supervisor.before(transformer)
                transformer(it).also {
                    supervisor.after(transformer)
                }

            }
            .onComplete(
                onSuccess = { result ->
                    supervisor.before(resultHandler)
                    resultHandler(result).also {
                        supervisor.after(resultHandler)
                    }
                },
                onFailure = { throwable ->
                    supervisor.before(errorHandler)
                    errorHandler(throwable).also {
                        supervisor.after(errorHandler)
                    }
                })
    }
}