package prod.prog.service.manager

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import io.github.cdimascio.dotenv.dotenv
import prod.prog.actionProperties.contextFactory.print.PrintInfo
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.BotCommand
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
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
    private val config = ConfigLoader.loadConfig()
    private val telegramBot = bot {
        token = telegramApiToken
        dispatch {
            command("start") {
                bot.sendMessage(
                    ChatId.fromId(message.chat.id),
                    text = "Welcome to the bot! Use /list to see company buttons."
                )
            }
            command("list") {
                val chatId = update.message?.chat?.id ?: return@command
                val buttons = config.buttons.map { InlineKeyboardButton.CallbackData(it.displayName, it.displayName) }
                val inlineKeyboardMarkup = InlineKeyboardMarkup.create(buttons.chunked(1))
                bot.sendMessage(
                    chatId = ChatId.fromId(chatId),
                    text = "Choose a company:",
                    replyMarkup = inlineKeyboardMarkup
                )
            }

            callbackQuery {
                val chatId = update.callbackQuery?.message?.chat?.id ?: return@callbackQuery
                val name = update.callbackQuery!!.data
                makeRequest(
                    RssSource(Company(name), NewsRss()),
                    IdTransformer(),
                    IgnoreHandler(),
                    IgnoreErrorHandler()
                ).thenApply { res ->
                    sendLargeMessage(chatId = ChatId.fromId(chatId), res)
                    val intro = if (res.isEmpty()) "No news for search" else "That is your result for search"
                    bot.sendMessage(
                        chatId = ChatId.fromId(chatId),
                        text = "$intro : \"$name\". \nSend /list to chose new company."
                    )
                }
            }
        }
    }
    init {
        setBotCommands()
    }
    private fun setBotCommands() {
        telegramBot.setMyCommands(
            listOf(
                BotCommand(command = "start", description = "Start the bot"),
                BotCommand(command = "list", description = "List available companies")
            )
        )
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