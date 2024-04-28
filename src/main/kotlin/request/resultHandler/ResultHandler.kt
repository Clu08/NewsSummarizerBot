package prod.prog.request.resultHandler

import prod.prog.actionProperties.Action
import prod.prog.actionProperties.print.PrintDebug

interface ResultHandler<T> : Action, PrintDebug {
    operator fun invoke(t: T)
}
