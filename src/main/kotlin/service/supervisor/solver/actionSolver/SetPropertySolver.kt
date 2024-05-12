package prod.prog.service.supervisor.solver.actionSolver

import prod.prog.actionProperties.Context
import prod.prog.service.supervisor.solver.Solver

interface SetPropertySolver : Solver<Context> {
    override fun invoke(t: Context): Context {
        return t.add(name() to value())
    }

    fun name(): String
    fun value(): Any

    companion object {
        operator fun invoke(name: String, value: Any) = object : SetPropertySolver {
            override fun name() = name
            override fun value() = value
        }
    }
}