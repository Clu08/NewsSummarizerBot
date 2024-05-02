package prod.prog.request

import prod.prog.actionProperties.Context

data class RequestContext(
    val requestSpecificContext: Context = Context(
        "id" to "0",
        "createdBy" to "System"
    ),
    val sourceContext: Context = Context(),
    val transformerContext: Context = Context(),
    val resultHandlerContext: Context = Context(),
    val errorHandlerContext: Context = Context(),
)