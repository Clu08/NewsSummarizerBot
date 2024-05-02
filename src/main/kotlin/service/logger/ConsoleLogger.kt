package prod.prog.service.logger

import prod.prog.actionProperties.print.PrintFatal
import prod.prog.actionProperties.print.PrintWarning

class ConsoleLogger : LoggerService {
    override fun name() = "ConsoleLogger"
    override fun log(logLevel: PrintFatal, message: String) {
        when (logLevel) {
            is PrintWarning -> System.out
            else -> System.err
        }.println("${logLevel.message()}\t${curTime()}\t\t$message")
    }
}