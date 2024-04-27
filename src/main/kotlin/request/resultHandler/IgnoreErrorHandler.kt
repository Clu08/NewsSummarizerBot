package prod.prog.request.resultHandler

class IgnoreErrorHandler : IgnoreHandler<Throwable>(), ErrorHandler {
    override fun message(): String = "Error ignored"
}