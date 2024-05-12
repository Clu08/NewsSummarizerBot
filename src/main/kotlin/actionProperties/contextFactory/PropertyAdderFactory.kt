package prod.prog.actionProperties.contextFactory

import prod.prog.actionProperties.Context

interface PropertyAdderFactory : ContextFactory {
    override fun invoke(initialContext: Context): Context =
        initialContext.set(key(), value())

    fun key(): String
    fun value(): Any
}