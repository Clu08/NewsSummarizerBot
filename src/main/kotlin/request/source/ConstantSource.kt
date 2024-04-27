package prod.prog.request.source

class ConstantSource<T>(private val t: T) : Source<T>() {
    override fun getSource(): T = t

    override fun message(): String = "ConstantSource $t"
}