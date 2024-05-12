package prod.prog.request.transformer

import prod.prog.actionProperties.Action
import prod.prog.actionProperties.contextFactory.print.PrintDebug

abstract class Transformer<T, R> : Action() {
    init {
        addContext(
            PrintDebug { message() },
        )
    }

    abstract operator fun invoke(t: T): R
    abstract fun message(): String
}
