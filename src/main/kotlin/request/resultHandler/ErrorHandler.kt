package prod.prog.request.resultHandler

import prod.prog.actionProperties.contextFactory.print.PrintError

abstract class ErrorHandler : ResultHandler<Throwable>() {
    init {
        addContext(PrintError { message() })
    }
}