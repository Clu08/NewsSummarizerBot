package prod.prog.service.supervisor

import prod.prog.actionProperties.Context
import prod.prog.service.Service
import prod.prog.service.supervisor.solver.Solver
import prod.prog.service.supervisor.solver.actionSolver.ContextCopySolver
import prod.prog.service.supervisor.solver.requestSolver.RequestContextSolver

class Supervisor(
    before: Solver<Context>,
    after: Solver<Context>,
    initContext: Solver<Context>,
) : Service {
    var before: Solver<Context> = ContextCopySolver(before)
        set(solver) {
            field = ContextCopySolver(solver)
        }

    var after: Solver<Context> = ContextCopySolver(after)
        set(solver) {
            field = ContextCopySolver(solver)
        }

    private var initContext: RequestContextSolver = RequestContextSolver(initContext)

    fun getInitContext() = initContext

    fun setInitContext(solver: Solver<Context>) {
        initContext = RequestContextSolver(solver)
    }

    override fun name() = "supervisor"
}
