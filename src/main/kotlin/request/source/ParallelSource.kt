package prod.prog.request.source

import io.github.vjames19.futures.jdk8.Future

class ParallelSource<T>(private vararg val sources: Source<T>) : Source<List<T>>() {
    override fun invoke(t: Unit): List<T> =
        Future.allAsList(sources.map { it() }).get()

    override fun message() = "ParallelSource(${sources.joinToString()})"
}