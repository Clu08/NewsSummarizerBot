package prod.prog.service.logger

import prod.prog.actionProperties.print.PrintFatal
import java.text.SimpleDateFormat
import java.util.*

interface LoggerService {
    fun log(logLevel: PrintFatal, message: String)

    fun curTime(): String {
        val dateFormat = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        return dateFormat.format(Date())
    }

    fun andThen(other: LoggerService): LoggerService =
        object : LoggerService {
            override fun log(logLevel: PrintFatal, message: String) {
                this@LoggerService.log(logLevel, message)
                other.log(logLevel, message)
            }
        }
}