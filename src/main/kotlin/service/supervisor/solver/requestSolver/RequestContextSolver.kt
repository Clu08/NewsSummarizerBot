package prod.prog.service.supervisor.solver.requestSolver

import prod.prog.actionProperties.Context
import prod.prog.request.RequestContext
import prod.prog.service.supervisor.solver.Solver
import prod.prog.service.supervisor.solver.actionSolver.ContextCopySolver

/**
 * Returns Initialized [RequestContext]
 *
 * Maps initial requestSpecificContext with [solver], then pushes it to all other [Contexts][Context].
 * It can rewrite [Contexts][Context] properties, it is intentional. Is automatically added
 * by [Supervisor][prod.prog.service.supervisor.Supervisor].
 */
class RequestContextSolver(solver: Solver<Context>) : Solver<RequestContext> {
    private val solver = ContextCopySolver(solver)

    override fun invoke(t: RequestContext): RequestContext {
        val requestSpecificContext = solver(t.requestSpecificContext)
        val result = RequestContext(
            requestSpecificContext,
            t.sourceContext.add(requestSpecificContext),
            t.transformerContext.add(requestSpecificContext),
            t.resultHandlerContext.add(requestSpecificContext),
            t.errorHandlerContext.add(requestSpecificContext),
        )
        return result
    }
}