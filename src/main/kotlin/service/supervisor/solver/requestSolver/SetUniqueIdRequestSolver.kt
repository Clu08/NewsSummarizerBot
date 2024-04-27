package prod.prog.service.supervisor.solver.requestSolver

import prod.prog.request.Request
import prod.prog.service.supervisor.solver.Solver
import java.util.concurrent.atomic.AtomicLong

class SetUniqueIdRequestSolver<T, R> : Solver<Request<T, R>> {
    private val id = AtomicLong(1L)

    override fun invoke(t: Request<T, R>): Request<T, R> {
        val curId = id.getAndIncrement()
        t.source.id = curId
        t.transformer.id = curId
        t.resultHandler.id = curId
        t.errorHandler.id = curId
        return t
    }
}