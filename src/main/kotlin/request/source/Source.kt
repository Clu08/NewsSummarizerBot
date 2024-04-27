package prod.prog.request.source

import io.github.vjames19.futures.jdk8.Future
import prod.prog.actionProperties.ActionClass
import prod.prog.actionProperties.print.PrintDebug
import java.util.concurrent.CompletableFuture

abstract class Source<T> : ActionClass(), PrintDebug {
    operator fun invoke(): CompletableFuture<T> =
        Future { getSource() }

    abstract fun getSource(): T
}
