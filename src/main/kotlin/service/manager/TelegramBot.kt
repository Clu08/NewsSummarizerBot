package prod.prog.service.manager

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import io.github.cdimascio.dotenv.dotenv
import prod.prog.actionProperties.contextFactory.print.PrintInfo
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.*
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.rss.AvailableRssSources
import prod.prog.dataTypes.rss.NewsPiecesByCompanyRssSource
import prod.prog.request.resultHandler.IgnoreErrorHandler
import prod.prog.request.resultHandler.IgnoreHandler
import prod.prog.request.transformer.IdTransformer
import prod.prog.service.database.DatabaseService
import prod.prog.service.logger.LoggerService
import prod.prog.service.rss.RssService
import prod.prog.service.supervisor.Supervisor
import java.io.Serializable

class TelegramBot(supervisor: Supervisor, private val logger: LoggerService, private val rssService : RssService, private val dataBase : DatabaseService) : RequestManager(supervisor) {
    override fun name() = "TelegramBot"
    private val telegramApiToken = dotenv()["TELEGRAM_API_TOKEN"]
    private val telegramApiAddress = dotenv()["TELEGRAM_API_ADDRESS"]
    private val telegramBot = bot {
        token = telegramApiToken
        val awaitingCompanyName = mutableMapOf<Long, Boolean>()
        dispatch {
            command("start") {
                val chatId = update.message?.chat?.id ?: return@command
                logMessage(update.message!!)
                logger.log(PrintInfo, "Received command: /start")
                bot.sendMessage(
                    ChatId.fromId(message.chat.id),
                    text = "Welcome to the bot! Use /list to see company buttons."
                )
                awaitingCompanyName[chatId] = false
            }
            command("list") {
                logMessage(update.message!!)
                logger.log(PrintInfo, "Received command: /list")
                val chatId = update.message?.chat?.id ?: return@command
                val buttons = dataBase.getAllCompanies().map { InlineKeyboardButton.CallbackData(it.name, it.name)  }
                val inlineKeyboardMarkup = InlineKeyboardMarkup.create(buttons.chunked(1))
                bot.sendMessage(
                    chatId = ChatId.fromId(chatId),
                    text = "Choose a company:",
                    replyMarkup = inlineKeyboardMarkup
                )
                awaitingCompanyName[chatId] = false
            }

            command("add") {
                val chatId = update.message?.chat?.id ?: return@command
                logMessage(update.message!!)
                logger.log(PrintInfo, "Received command: /add")
                bot.sendMessage(
                    chatId = ChatId.fromId(chatId),
                    text = "Please enter the name of the company you want to add."
                )
                awaitingCompanyName[chatId] = true
            }

            text {
                val chatId = message.chat.id
                val text = message.text ?: ""
                if (text.startsWith("/")) return@text // Ignore other commands
                if (awaitingCompanyName[chatId] != true) {
                    return@text
                }

                awaitingCompanyName[chatId] = false


                val companyName = text.trim()
                if (companyName.isEmpty() || companyName.any { !it.isLetterOrDigit() && it != '-' && !it.isWhitespace() }) {
                    bot.sendMessage(
                        chatId = ChatId.fromId(chatId),
                        text = "Invalid company name. Please enter a valid company name."
                    )
                } else {
                    if (dataBase.getCompanyByName(companyName) == null) {
                        dataBase.addCompany(companyName)
                        bot.sendMessage(
                            chatId = ChatId.fromId(chatId),
                            text = "Company \"$companyName\" added successfully!"
                        )
                        logMessage(message)
                        logger.log(PrintInfo, "Added company: $companyName")
                    } else {
                        bot.sendMessage(
                            chatId = ChatId.fromId(chatId),
                            text = "Company \"$companyName\" already exists in the database."
                        )
                        logMessage(message)
                        logger.log(PrintInfo, "Company already exists: $companyName")
                    }
                }
            }

            callbackQuery {
                val chatId = update.callbackQuery?.message?.chat?.id ?: return@callbackQuery
                val name = update.callbackQuery!!.data
                logMessage(update.callbackQuery!!.message!!)
                logger.log(PrintInfo, "Callback query with data: $name")
                makeRequest(
                    NewsPiecesByCompanyRssSource(company = Company(name), rssService = rssService, rssNewsLinks = AvailableRssSources.entries.map { s -> s.rssNewsLink }),
                    IdTransformer(),
                    IgnoreHandler(),
                    IgnoreErrorHandler()
                ).thenApply { res ->
                    sendLargeMessage(chatId = ChatId.fromId(chatId), res)
                    val intro = if (res.isEmpty()) "No news for search" else "That is your result for search"
                    logger.log(PrintInfo, if (res.isEmpty()) "No info for $name found" else "Info for $name not empty")
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
                BotCommand(command = "list", description = "List available companies"),
                BotCommand(command = "add", description = "Add company name to list")
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

    private fun sendLargeMessage(chatId: ChatId, textList: List<Serializable>, beg: Int = 0) {
        if (beg >= textList.size) {
            return
        }
        val maxLen = 4095
        val text = StringBuilder()

        for (i in beg until textList.size) {
            val newLine = textList[i].toString()
            if (text.length + newLine.length < maxLen) {
                text.append(newLine)
            } else {
                telegramBot.sendMessage(chatId = chatId, text = text.toString(), parseMode = ParseMode.MARKDOWN_V2)
                logger.log(PrintInfo, "Bot sent message fragment $i")
                sendLargeMessage(chatId, textList, i)
                return
            }
        }
        telegramBot.sendMessage(chatId = chatId, text = text.toString(), parseMode = ParseMode.MARKDOWN_V2)
        logger.log(PrintInfo, "Bot sent full message")

    }

    private fun logMessage(message: Message) {
        logger.log(
            PrintInfo,
            "Bot text request: [${message.text}], chatId: ${message.chat.id}, author: ${message.authorSignature}"
        )
    }
}