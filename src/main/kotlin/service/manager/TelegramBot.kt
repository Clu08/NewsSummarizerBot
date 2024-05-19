package prod.prog.service.manager

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.handlers.Handler
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import io.github.cdimascio.dotenv.dotenv
import prod.prog.actionProperties.contextFactory.print.PrintInfo
import prod.prog.request.resultHandler.IgnoreErrorHandler
import prod.prog.request.resultHandler.IgnoreHandler
import prod.prog.request.source.ConstantSource
import prod.prog.request.transformer.IdTransformer
import prod.prog.service.logger.LoggerService
import prod.prog.service.supervisor.Supervisor

class TelegramBot(supervisor: Supervisor, private val logger: LoggerService) : RequestManager(supervisor) {
    override fun name() = "TelegramBot"
    private val telegramApiToken = dotenv()["TELEGRAM_API_TOKEN"]
    private val telegramApiAddress = dotenv()["TELEGRAM_API_ADDRESS"]
    private val telegramBot = bot {
        token = telegramApiToken
        dispatch {
            text {
                handleTextRequest()
            }
        }
    }

    override fun start() {
        telegramBot.startPolling()
        logger.log(PrintInfo, "polling started for $telegramApiAddress")
    }

    override fun stop() {
        telegramBot.stopPolling()
        logger.log(PrintInfo, "polling stopped for $telegramApiAddress")
    }

    private fun TextHandlerEnvironment.handleTextRequest() {
        val chatId = ChatId.fromId(message.chat.id)

        logMessage(message)

        makeRequest(
            ConstantSource("hi, $text!"),
            IdTransformer(),
            IgnoreHandler(),
            IgnoreErrorHandler()
        ).thenApply { res ->
            bot.sendMessage(chatId, text = res)
        }
    }

    private fun logMessage(message: Message) {
        logger.log(PrintInfo, "Bot text request: [${message.text}], chatId: ${message.chat.id}, author: ${message.authorSignature}")
    }
}