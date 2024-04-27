package prod.prog.request.transformer

import prod.prog.actionProperties.print.PrintDebug

open class IdTransformer<T> : Transformer<T, T>, PrintDebug {
    override fun invoke(t: T): T = t

    override fun message(): String = "IdTransformer"
}