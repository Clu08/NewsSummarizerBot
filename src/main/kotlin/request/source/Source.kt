package prod.prog.request.source

import io.github.vjames19.futures.jdk8.Future
import io.github.vjames19.futures.jdk8.map
import io.github.vjames19.futures.jdk8.zip
import prod.prog.actionProperties.Action
import prod.prog.actionProperties.print.PrintDebug
import java.util.concurrent.CompletableFuture

interface Source<T> : Action, PrintDebug {
    operator fun invoke(): CompletableFuture<T> =
        Future { getSource() }

    fun getSource(): T

    // todo think how to combine interfaces
    fun <R> pairWith(other: Source<R>): Source<Pair<T, R>> =
        object : Source<Pair<T, R>> {
            override fun invoke(): CompletableFuture<Pair<T, R>> =
                this@Source().zip(other()).map { (first, second) -> Pair(first, second) }

            override fun getSource(): Pair<T, R> =
                invoke().get()

            override fun message() = "Pair(${this@Source.message()}, ${other.message()})"
        }

    // todo think how to save and combine interfaces
    fun <R> andThen(next: (T) -> Source<R>): Source<R> =
        object : Source<R> {
            override fun invoke(): CompletableFuture<R> =
                this@Source().map { next(it).getSource() }

            override fun getSource(): R =
                invoke().get()

            override fun message() = "${this@Source.message()} -> ${next::class})"
        }

    // could be done using pair with and Id
    fun <R> andThenWithPair(next: (T) -> Source<R>): Source<Pair<T, R>> =
        object : Source<Pair<T, R>> {
            override fun invoke(): CompletableFuture<Pair<T, R>> =
                this@Source().map { Pair(it, next(it).getSource()) }

            override fun getSource(): Pair<T, R> =
                invoke().get()

            override fun message() = "${this@Source.message()} -> Pair ${next::class})"
        }
}
