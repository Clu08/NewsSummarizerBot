package prod.prog.actionProperties

interface Action {
    fun addContext(context: ActionContext): ActionWithContext<Action> =
        ActionWithContext(context, this)
}