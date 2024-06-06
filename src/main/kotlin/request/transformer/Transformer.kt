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

    fun <K> withPair(other: Transformer<T, K>): Transformer<T, Pair<R, K>> =
        object : Transformer<T, Pair<R, K>>() {
            init {
                combineContext(this@Transformer, other)
                addContext(PrintDebug { message() })
            }

            override fun invoke(t: T): Pair<R, K> =
                Pair(this@Transformer(t), other(t))

            override fun message() = "Pair(${this@Transformer.message()}, ${other.message()})"
        }

    fun <K> andThen(next: Transformer<R, K>): Transformer<T, K> =
        object : Transformer<T, K>() {
            init {
                combineContext(this@Transformer, next)
                addContext(PrintDebug { message() })
            }

            override fun invoke(t: T): K =
                next(this@Transformer(t))

            override fun message() = "${this@Transformer.message()} -> ${next.message()})"
        }

    fun <K> andThenWithPair(next: Transformer<R, K>): Transformer<T, Pair<R, K>> =
        this@Transformer.withPair(this@Transformer.andThen(next))
}
