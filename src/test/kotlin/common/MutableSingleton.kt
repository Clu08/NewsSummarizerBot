package prod.prog.common

class MutableSingleton<T>(private var x: T) {
    override fun toString() = "Ref($x)"
}