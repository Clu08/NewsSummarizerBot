package prod.prog.service.supervisor

import prod.prog.actionProperties.Action
import prod.prog.actionProperties.ActionWithContext
import prod.prog.request.RequestContext
import prod.prog.service.supervisor.solver.Solver

class Supervisor(
    var before: Solver<ActionWithContext<out Action>>,
    var after: Solver<ActionWithContext<out Action>>,
    var initContext: Solver<RequestContext>,
)
