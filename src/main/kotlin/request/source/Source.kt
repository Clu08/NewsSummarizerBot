package prod.prog.request.source

import io.github.vjames19.futures.jdk8.Future
import prod.prog.actionProperties.contextFactory.print.PrintDebug
import prod.prog.request.transformer.Transformer

typealias Source<T> = Transformer<Unit, T>

operator fun <T> Source<T>.invoke() =
    Future { invoke(Unit) }

fun <T, R> Source<R>.ignoreInput(): Transformer<T, R> = object : Transformer<T, R>() {
    init {
        copyContext(this@ignoreInput)
        addContext(PrintDebug { message() })
    }

    override fun invoke(t: T): R = this@ignoreInput(Unit)

    override fun message() = this@ignoreInput.message()
}