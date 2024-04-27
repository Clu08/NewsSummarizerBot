package prod.prog.service.logger

import prod.prog.actionProperties.print.PrintFatal
import prod.prog.actionProperties.print.PrintWarning
import java.text.SimpleDateFormat
import java.util.*

class ConsoleLogger : LoggerService {
    override fun log(logLevel: PrintFatal, message: String) {
        val dateFormat = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = dateFormat.format(Date())

        when (logLevel) {
            is PrintWarning -> System.out
            else -> System.err
        }.println("${logLevel.message()}\t${currentDate}\t\t$message")
    }
}