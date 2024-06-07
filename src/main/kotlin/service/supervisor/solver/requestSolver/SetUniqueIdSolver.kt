package prod.prog.service.supervisor.solver.requestSolver

import prod.prog.service.supervisor.solver.actionSolver.SetPropertySolver
import java.util.concurrent.atomic.AtomicLong

class SetUniqueIdSolver(from: Long = 1L) : SetPropertySolver {
    private val id = AtomicLong(from)
    override fun name() = "id"
    override fun value(): Long {
        return id.getAndIncrement()
    }
}