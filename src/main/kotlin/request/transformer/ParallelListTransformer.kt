package prod.prog.request.transformer

import io.github.vjames19.futures.jdk8.Future

class ParallelListTransformer<T, R>(private val transformer: Transformer<T, R>) : Transformer<List<T>, List<R>>() {
    override fun invoke(t: List<T>): List<R> =
        Future.allAsList(t.map { Future { transformer(it) } }).get()

    override fun message() = "ParallelTransformer(${transformer.message()})"
}