package prod.prog.request.transformer

import prod.prog.actionProperties.ActionClass

abstract class Transformer<T, R> : ActionClass() {
    abstract operator fun invoke(t: T): R
}
