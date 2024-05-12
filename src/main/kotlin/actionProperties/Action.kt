package prod.prog.actionProperties

import prod.prog.actionProperties.contextFactory.ContextFactory

/**
 * Provides a logical part of [Request][prod.prog.request.Request] with [Context] for [Supervisor][prod.prog.service.supervisor.Supervisor]
 *
 * [Action] should be almost pure, without inner state and should only make changes in services it works with.
 * But [Context] could be used by [Supervisor][prod.prog.service.supervisor.Supervisor] objects.
 */
abstract class Action {
    private val contextFactories = mutableListOf<ContextFactory>()

    fun copyContext(action: Action) {
        addContext(*action.getContextFactories())
    }

    fun combineContext(action: Action, other: Action) {
        addContext(
            *action.getContextFactories(),
            *other.getContextFactories()
        )
    }

    private fun getContextFactories() = contextFactories.toTypedArray()

    fun addContext(vararg factories: ContextFactory) {
        contextFactories.addAll(factories)
    }

    fun getContext() =
        getContext(Context())

    fun getContext(context: Context) =
        contextFactories.fold(context) { accumulatedContext, factory ->
            factory(accumulatedContext)
        }
}