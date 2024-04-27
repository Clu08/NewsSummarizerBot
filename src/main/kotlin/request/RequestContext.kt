package prod.prog.request

import prod.prog.actionProperties.ActionContext

data class RequestContext(
    val sourceContext: ActionContext,
    val transformerContext: ActionContext,
    val resultHandlerContext: ActionContext,
    val errorHandlerContext: ActionContext,
) {
    fun map(map: (ActionContext) -> ActionContext): RequestContext =
        RequestContext(
            map(sourceContext),
            map(transformerContext),
            map(resultHandlerContext),
            map(errorHandlerContext)
        )

    companion object {
        operator fun invoke() =
            RequestContext(
                ActionContext.system(),
                ActionContext.system(),
                ActionContext.system(),
                ActionContext.system()
            )
    }
}