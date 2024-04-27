package prod.prog.actionProperties

/**
 * Provides [Action] with a [ActionContext]
 *
 * [Action] should be almost *pure*, without inner state and only making changes in services it works with.
 * But [ActionContext] could be used by [Supervisor][prod.prog.service.supervisor.Supervisor] objects.
 */
open class ActionWithContext<T : Action>(val context: ActionContext, val action: T)