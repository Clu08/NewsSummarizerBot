package prod.prog.request.resultHandler

class IgnoreErrorHandler : ErrorHandler() {
    override fun invoke(t: Throwable) {}

    override fun message(): String = "error ignored"
}