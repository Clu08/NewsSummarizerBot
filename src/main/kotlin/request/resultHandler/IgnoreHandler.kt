package prod.prog.request.resultHandler

open class IgnoreHandler<T> : ResultHandler<T>() {
    override fun invoke(t: T) {}
    override fun message(): String = "result ignored"
}