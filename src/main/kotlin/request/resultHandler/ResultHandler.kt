package prod.prog.request.resultHandler

import prod.prog.actionProperties.ActionClass

abstract class ResultHandler<T> : ActionClass() {
    abstract operator fun invoke(t: T)
}
