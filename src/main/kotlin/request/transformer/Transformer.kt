package prod.prog.request.transformer

import prod.prog.actionProperties.Action
import prod.prog.actionProperties.print.PrintDebug

interface Transformer<T, R> : Action, PrintDebug {
    operator fun invoke(t: T): R
}
