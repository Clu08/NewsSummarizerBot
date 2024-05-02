package prod.prog.service.manager

import prod.prog.request.Request
import prod.prog.request.RequestContext
import prod.prog.request.resultHandler.ResultHandler
import prod.prog.request.source.Source
import prod.prog.request.transformer.Transformer
import prod.prog.service.Service
import prod.prog.service.supervisor.Supervisor
import java.util.concurrent.CompletableFuture

abstract class RequestManager(private val supervisor: Supervisor) : Service {
    abstract fun start()

    abstract fun stop()

    protected fun <T, R> makeRequest(
        source: Source<T>,
        transformer: Transformer<T, R>,
        resultHandler: ResultHandler<R>,
        errorHandler: ResultHandler<Throwable>,
    ): CompletableFuture<R> =
        makeRequest(
            Request(
                source,
                transformer,
                resultHandler,
                errorHandler,
                RequestContext()
            )
        )

    protected fun <T, R> makeRequest(request: Request<T, R>): CompletableFuture<R> {
        return request
            .addToInitContext("createdBy" to name())
            .run(supervisor)
    }
}