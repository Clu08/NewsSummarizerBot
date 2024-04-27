package prod.prog.request.transformer

import prod.prog.actionProperties.Action

interface Transformer<T, R> : Action {
    operator fun invoke(t: T): R
}
