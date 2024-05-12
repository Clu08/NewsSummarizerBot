package prod.prog.actionProperties

data class Context(val map: Map<String, Any>) {
    constructor(vararg elements: Pair<String, Any>) : this(hashMapOf(*elements))

    fun add(vararg elements: Pair<String, Any>): Context =
        Context(map.plus(mapOf(*elements)))

    fun add(other: Context): Context =
        add(*other.toArray())

    operator fun get(key: String) = map[key]

    fun set(key: String, value: Any): Context =
        Context(map.plus(key to value))

    fun has(key: String) = map.containsKey(key)

    fun toArray() = map.toList().toTypedArray()

    fun diff(otherContext: Context): Context = Context(
        map.filter { (key, value) ->
            if (otherContext.has(key)) {
                value != otherContext[key]
            } else {
                true
            }
        }
    )
}

