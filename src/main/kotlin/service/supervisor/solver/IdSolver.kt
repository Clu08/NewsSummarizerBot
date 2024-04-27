package prod.prog.service.supervisor.solver

interface IdSolver<T> : Solver<T> {
    override operator fun invoke(t: T): T =
        t.also { solve(t) }

    fun solve(t: T)
}