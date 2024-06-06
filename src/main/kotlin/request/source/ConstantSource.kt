package prod.prog.request.source

class ConstantSource<T>(private val constant: T) : Source<T>() {
    override fun invoke(t: Unit): T = constant

    override fun message(): String = "ConstantSource $constant"
}