package prod.prog.request.resultHandler

import prod.prog.actionProperties.contextFactory.print.PrintError
import prod.prog.service.logger.LoggerService

class PrintErrorHandler(val logger: LoggerService) : ErrorHandler() {
    override fun invoke(t: Throwable) {
        t.message?.let { logger.log(PrintError, it) }
    }

    override fun message() = "PrintErrorHandler"
}