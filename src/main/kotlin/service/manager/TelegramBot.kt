package prod.prog.service.manager

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import io.github.cdimascio.dotenv.dotenv
import prod.prog.actionProperties.contextFactory.print.PrintInfo
import prod.prog.request.resultHandler.IgnoreErrorHandler
import prod.prog.request.resultHandler.IgnoreHandler
import prod.prog.request.transformer.IdTransformer
import prod.prog.service.logger.LoggerService
import prod.prog.service.supervisor.Supervisor
import prod.prog.dataTypes.Company
import prod.prog.request.source.rss.RssSource
import prod.prog.service.rss.NewsRss
import java.io.Serializable

class TelegramBot(supervisor: Supervisor, var logger: LoggerService) : RequestManager(supervisor) {
    override fun name() = "TelegramBot"
    private val telegramApiToken = dotenv()["TELEGRAM_API_TOKEN"]
    private val telegramApiAddress = dotenv()["TELEGRAM_API_ADDRESS"]
    private val telegramBot = bot {
        token = telegramApiToken
        dispatch {
            text {
                makeRequest(
                    RssSource(Company(text), NewsRss()),
                    IdTransformer(),
                    IgnoreHandler(),
                    IgnoreErrorHandler()
                ).thenApply { res ->
                    sendLargeMessage(ChatId.fromId(message.chat.id), res)
                    val intro = if (res.isEmpty()) "No news for search" else "That is your result for search"
                    bot.sendMessage(
                        ChatId.fromId(message.chat.id),
                        text = "$intro : \"$text\". \nSend your key word to find news about it."
                    )
                }
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

    fun sendLargeMessage(chatId: ChatId, textList: List<Serializable>, beg: Int = 0) {
        if (beg >= textList.size) {
            return
        }
        val maxLen = 4095
        var text = StringBuilder()

        for (i in beg until textList.size) {
            val newLine = textList[i].toString()
            if (text.length + newLine.length < maxLen) {
                text.append(newLine)
            } else {
                telegramBot.sendMessage(chatId = chatId, text = text.toString())
                sendLargeMessage(chatId, textList, i)
                return
            }
        }
        telegramBot.sendMessage(chatId = chatId, text = text.toString())
    }
}