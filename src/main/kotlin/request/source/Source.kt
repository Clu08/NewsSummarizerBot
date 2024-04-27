package prod.prog.request.source

import io.github.vjames19.futures.jdk8.Future
import prod.prog.actionProperties.Action
import prod.prog.actionProperties.print.PrintDebug
import java.util.concurrent.CompletableFuture

interface Source<T> : Action, PrintDebug {
    operator fun invoke(): CompletableFuture<T> =
        Future { getSource() }

    fun getSource(): T
}
