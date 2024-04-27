package prod.prog.request.resultHandler

import prod.prog.actionProperties.print.PrintError

abstract class ErrorHandler : ResultHandler<Throwable>(), PrintError