package prod.prog.service.supervisor

import prod.prog.actionProperties.Action
import prod.prog.request.Request
import prod.prog.service.supervisor.solver.Solver

class Supervisor(
    var before: Solver<Action>,
    var after: Solver<Action>,
    var onRegister: Solver<Request<Nothing, Nothing>>,
) {
    // No ideas how to build same logic without unchecked cast :(
    fun <T, R> register(request: Request<T, R>): Request<T, R> {
        @Suppress("UNCHECKED_CAST")
        return onRegister(request as Request<Nothing, Nothing>) as Request<T, R>
    }
}
