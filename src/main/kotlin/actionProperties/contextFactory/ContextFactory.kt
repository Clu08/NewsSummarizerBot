package prod.prog.actionProperties.contextFactory

import prod.prog.actionProperties.Context

interface ContextFactory {
    operator fun invoke(initialContext: Context): Context
}