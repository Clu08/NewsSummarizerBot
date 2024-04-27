package prod.prog.service.supervisor.solver.requestSolver

import prod.prog.request.RequestContext
import prod.prog.service.supervisor.solver.Solver
import java.util.concurrent.atomic.AtomicLong

class SetUniqueIdRequestSolver : Solver<RequestContext> {
    private val id = AtomicLong(1L)

    override fun invoke(t: RequestContext): RequestContext {
        val curId = id.getAndIncrement()
        return t.map { it.copy(id = curId) }
    }
}