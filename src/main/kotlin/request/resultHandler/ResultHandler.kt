package prod.prog.request.resultHandler

import prod.prog.actionProperties.Action

interface ResultHandler<T> : Action {
    operator fun invoke(t: T)
}
