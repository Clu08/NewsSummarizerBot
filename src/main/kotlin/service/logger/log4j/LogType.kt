package prod.prog.service.logger.log4j

enum class LogType(val logName: String) {
    MESSAGES("APPLICATION_MESSAGES.log"),
    TELEGRAM_API("TELEGRAM_API.log"),
}