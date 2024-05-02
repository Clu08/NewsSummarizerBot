package prod.prog.service.manager

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import io.github.cdimascio.dotenv.dotenv
import prod.prog.actionProperties.print.PrintInfo
import prod.prog.request.resultHandler.IgnoreErrorHandler
import prod.prog.request.resultHandler.IgnoreHandler
import prod.prog.request.source.ConstantSource
import prod.prog.request.transformer.IdTransformer
import prod.prog.service.logger.LoggerService
import prod.prog.service.supervisor.Supervisor

class TelegramBot(supervisor: Supervisor, var logger: LoggerService) : RequestManager(supervisor) {
    override fun name() = "TelegramBot"
    private val telegramApiToken = dotenv()["TELEGRAM_API_TOKEN"]
    private val telegramApiAddress = dotenv()["TELEGRAM_API_ADDRESS"]
    private val telegramBot = bot {
        token = telegramApiToken
        dispatch {
            text {
                makeRequest(
                    ConstantSource("hi, $text!"),
                    IdTransformer(),
                    IgnoreHandler(),
                    IgnoreErrorHandler()
                ).thenApply { res ->
                    bot.sendMessage(ChatId.fromId(message.chat.id), text = res)
                }
            }
        }
    }

    override fun start() {
        telegramBot.startPolling()
        logger.log(PrintInfo(), "polling started for $telegramApiAddress")
    }

    override fun stop() {
        telegramBot.stopPolling()
        logger.log(PrintInfo(), "polling stopped for $telegramApiAddress")
    }
}