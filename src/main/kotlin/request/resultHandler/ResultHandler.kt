package prod.prog.request.resultHandler

import prod.prog.actionProperties.contextFactory.print.PrintDebug
import prod.prog.request.transformer.Transformer

typealias ResultHandler<T> = Transformer<T, Unit>

fun <T, R> Transformer<T, R>.ignoreOutput(): ResultHandler<T> = object : ResultHandler<T>() {
    init {
        copyContext(this@ignoreOutput)
        addContext(PrintDebug { message() })
    }

    override fun invoke(t: T) {
        this@ignoreOutput(t)
    }

    override fun message() = this@ignoreOutput.message()
}