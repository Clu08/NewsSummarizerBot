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
) {
    fun run(supervisor: Supervisor): CompletableFuture<R> {
        val (source, transformer, resultHandler, errorHandler) = supervisor.register(this)

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