package prod.prog.service.logger.log4j

import org.apache.logging.log4j.kotlin.logger
import prod.prog.actionProperties.contextFactory.print.*
import prod.prog.service.logger.LoggerService

class Log4jLoggerService(logType: LogType) : LoggerService {
    private val logger = logger(logType.logName)

    override fun name() = "Log4jLoggerService"

    override fun log(logLevel: PrintFatal, message: String) {
        when (logLevel) {
            is PrintDebug -> {
                logger.debug(message)
            }
            is PrintInfo -> {
                logger.info(message)
            }
            is PrintWarning -> {
                logger.warn(message)
            }
            is PrintError -> {
                logger.error(message)
            }
            is PrintTrace -> {
                logger.trace(message)
            }
            is PrintFatal -> {
                logger.fatal(message)
            }
        }
    }
}
