package prod.prog.request.resultHandler

import prod.prog.actionProperties.Action
import prod.prog.actionProperties.contextFactory.print.PrintDebug

abstract class ResultHandler<T> : Action() {
    init {
        addContext(PrintDebug { message() })
    }

    abstract operator fun invoke(t: T)
    abstract fun message(): String
}
