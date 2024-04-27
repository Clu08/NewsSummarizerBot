package prod.prog.service.manager

import prod.prog.request.Request
import prod.prog.request.resultHandler.ResultHandler
import prod.prog.request.source.Source
import prod.prog.request.transformer.Transformer
import prod.prog.service.supervisor.Supervisor
import java.util.concurrent.CompletableFuture

abstract class RequestManager(private val supervisor: Supervisor, val managerName: String) {
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
                errorHandler
            )
        )

    protected fun <T, R> makeRequest(request: Request<T, R>): CompletableFuture<R> {
        request.source.createdBy = managerName
        request.transformer.createdBy = managerName
        request.resultHandler.createdBy = managerName
        request.errorHandler.createdBy = managerName
        return request.run(supervisor)
    }
}