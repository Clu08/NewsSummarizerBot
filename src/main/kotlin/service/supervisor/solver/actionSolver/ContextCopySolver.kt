package prod.prog.service.supervisor.solver.actionSolver

import prod.prog.actionProperties.Context
import prod.prog.service.supervisor.solver.Solver

/**
 * Copies [Context] for use by other [Solver]'s
 *
 * Is automatically added by [Supervisor][prod.prog.service.supervisor.Supervisor]
 * and [RequestContextSolver][prod.prog.service.supervisor.solver.requestSolver.RequestContextSolver].
 */
class ContextCopySolver(val solver: Solver<Context>) : Solver<Context> {
    override fun invoke(t: Context): Context {
        return solver(t.copy())
    }
}