package prod.prog.service.supervisor.solver

class EmptySolver<T> : IdSolver<T> {
    override fun solve(t: T) {}
}