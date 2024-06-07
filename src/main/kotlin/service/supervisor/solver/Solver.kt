package prod.prog.service.supervisor.solver


fun interface Solver<T> {
    operator fun invoke(t: T): T

    fun andThen(other: Solver<T>): Solver<T> =
        Solver { t ->
            other.invoke(this.invoke(t))
        }
}