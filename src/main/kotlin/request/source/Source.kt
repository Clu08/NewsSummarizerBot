package prod.prog.request.source

import io.github.vjames19.futures.jdk8.Future
import prod.prog.request.transformer.Transformer

typealias Source<T> = Transformer<Unit, T>

operator fun <T> Source<T>.invoke() =
    Future { invoke(Unit) }