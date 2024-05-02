package prod.prog.request.source

import io.github.vjames19.futures.jdk8.Future
import io.github.vjames19.futures.jdk8.map
import io.github.vjames19.futures.jdk8.zip
import prod.prog.actionProperties.Action
import prod.prog.actionProperties.contextFactory.print.PrintDebug
import java.util.concurrent.CompletableFuture

abstract class Source<T> : Action() {
    init {
        addContext(PrintDebug { message() })
    }

    open operator fun invoke(): CompletableFuture<T> =
        Future { getSource() }

    abstract fun getSource(): T
    abstract fun message(): String

    // todo think how to combine interfaces
    fun <R> pairWith(other: Source<R>): Source<Pair<T, R>> =
        object : Source<Pair<T, R>>() {
            init {
                combineContext(this@Source, other)
                addContext(PrintDebug { message() })
            }

            override fun invoke(): CompletableFuture<Pair<T, R>> =
                this@Source().zip(other()).map { (first, second) -> Pair(first, second) }

            override fun getSource(): Pair<T, R> =
                invoke().get()

            override fun message() = "Pair(${this@Source.message()}, ${other.message()})"
        }

    // next source context won't be added!
    fun <R> andThen(next: (T) -> Source<R>): Source<R> =
        object : Source<R>() {
            init {
                copyContext(this@Source)
                addContext(PrintDebug { message() })
            }

            override fun invoke(): CompletableFuture<R> =
                this@Source().map { next(it).getSource() }

            override fun getSource(): R =
                invoke().get()

            override fun message() = "${this@Source.message()} -> ${next::class})"
        }

    // next source context won't be added!
    fun <R> andThenWithPair(next: (T) -> Source<R>): Source<Pair<T, R>> =
        this@Source.pairWith(this@Source.andThen(next))
}
