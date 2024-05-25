package prod.prog.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun <T, V> Iterable<T>.parallelMap(func: suspend (T) -> V): Iterable<V> =
    coroutineScope {
        map { element ->
            async(Dispatchers.IO) { func(element) }
        }.awaitAll()
    }
