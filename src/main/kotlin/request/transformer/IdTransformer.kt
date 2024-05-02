package prod.prog.request.transformer

open class IdTransformer<T> : Transformer<T, T>() {
    override fun invoke(t: T): T = t

    override fun message(): String = "IdTransformer"
}