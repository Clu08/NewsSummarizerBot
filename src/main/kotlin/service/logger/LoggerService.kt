package prod.prog.service.logger

import prod.prog.actionProperties.print.PrintFatal

interface LoggerService {
    fun log(logLevel: PrintFatal, message: String)

    fun andThen(other: LoggerService): LoggerService =
        object : LoggerService {
            override fun log(logLevel: PrintFatal, message: String) {
                this@LoggerService.log(logLevel, message)
                other.log(logLevel, message)
            }
        }
}