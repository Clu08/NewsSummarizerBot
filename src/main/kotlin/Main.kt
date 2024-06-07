package prod.prog

import prod.prog.actionProperties.contextFactory.print.*
import prod.prog.configuration.ApplicationConfiguration
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.request.Request
import prod.prog.request.RequestContext
import prod.prog.request.resultHandler.IgnoreErrorHandler
import prod.prog.request.resultHandler.IgnoreHandler
import prod.prog.request.source.ConstantSource
import prod.prog.request.transformer.IdTransformer
import prod.prog.request.transformer.LanguageModelTransformer
import prod.prog.service.database.DatabaseImpl
import prod.prog.service.database.DatabaseService
import prod.prog.service.database.DatabaseURL
import prod.prog.service.languageModel.YandexGptLanguageModel
import prod.prog.service.logger.log4j.Log4jLoggerService
import prod.prog.service.logger.log4j.LogType
import prod.prog.service.manager.DefaultCompanies
import prod.prog.service.manager.TelegramBot
import prod.prog.service.manager.rssManager
import prod.prog.service.newsFilter.NewsFilterByTextService
import prod.prog.service.rss.RssServiceImpl
import prod.prog.service.supervisor.Supervisor
import prod.prog.service.supervisor.solver.EmptySolver
import prod.prog.service.supervisor.solver.actionSolver.LoggerSolver
import prod.prog.service.supervisor.solver.actionSolver.ServiceLogSolver
import prod.prog.service.supervisor.solver.requestSolver.SetUniqueIdSolver
import javax.xml.parsers.DocumentBuilderFactory


fun main() {
    ApplicationConfiguration().initApplication()
    val logger = Log4jLoggerService(LogType.MESSAGES)
    val telegramApiLogger = Log4jLoggerService(LogType.TELEGRAM_API)

    val newsFilter = NewsFilterByTextService()
    val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val rssService = RssServiceImpl(logger, newsFilter, documentBuilder)
    val database = DatabaseService(DatabaseImpl(DatabaseURL.MAIN))

    val languageModel = YandexGptLanguageModel()
//    val languageModel = IdLanguageModel()

    val supervisor = Supervisor(
        before = LoggerSolver(logger, "started ", PrintInfo)
            .andThen(ServiceLogSolver("started")),
        after = LoggerSolver(logger, "finished", PrintInfo)
            .andThen(ServiceLogSolver("finished")),
        initContext = SetUniqueIdSolver()
    )

    val rssManager = rssManager(supervisor, logger, database, rssService, languageModel, 20 * 1_000)
    rssManager.start()

    if (database.getAllCompanies().toList().isEmpty()) {
        DefaultCompanies.entries.map { database.addCompany(it.displayName) }
    }
    val telegramBot = TelegramBot(supervisor, telegramApiLogger, database)
    telegramBot.start()
}

fun example() {
    ApplicationConfiguration().initApplication()
    val logger = Log4jLoggerService(LogType.MESSAGES)
    val telegramApiLogger = Log4jLoggerService(LogType.TELEGRAM_API)

    val newsFilter = NewsFilterByTextService()
    val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val rssService = RssServiceImpl(logger, newsFilter, documentBuilder)
    val dataBase = DatabaseService(DatabaseImpl(DatabaseURL.MAIN))

    val languageModel = YandexGptLanguageModel()

    println(
        Request.basicTransformerRequest(LanguageModelTransformer(languageModel))(
            Pair(
                Company("Nvidia"),
                NewsPiece(
                    "https://habr.com/ru/news/818993/",
                    "Nvidia анонсировала ИИ-помощника Project G-Assist для геймеров",
                    "На выставке Computex 2024 Nvidia представила Project G-Assist — технологию искусственного интеллекта на базе RTX, которая обеспечит геймерам контекстно-зависимую помощь в компьютерных играх и приложениях. Она охватывает миллионы руководств."
                )
            )
        )
    )

    // инициализируем Supervisor двумя функциями - что делать перед и после каждого экшена
    // внутри Solver<Action> будут использоваться "тэги" - интерфейсы из package actionProperties
    // тут для примера я привёл логгинг, но поведение может быть сколь угодно сложным
    val supervisor = Supervisor(
        before = LoggerSolver(logger, "started ", PrintError),
        // Два Solver можно последовательно соединить
        after = LoggerSolver(logger, "finished", PrintDebug)
            .andThen(LoggerSolver(logger, "after   ", PrintError)),
        initContext = SetUniqueIdSolver()
    )

    // создаём Request
    val request = Request(
        ConstantSource(1),
        IdTransformer(),
        //  вывод уровня Error напечатают все 3 логгера, а Debug только finished
        object : IgnoreHandler<Int>() {
            init {
                addContext(PrintError { message() })
            }

            override fun message() = "IgnoreHandler"
        },
        IgnoreErrorHandler(),
        RequestContext()
    )

    // в выводе видно, что результат пришёл до начала срабатывания Handler
    logger.log(PrintInfo, "result: ${request.get(supervisor)}")

    logger.log(PrintTrace, "trace")
    logger.log(PrintWarning, "warn")
    logger.log(PrintError, "error")

    telegramApiLogger.log(PrintInfo, "telegram api log")
    telegramApiLogger.log(PrintWarning, "telegram api warn")
    telegramApiLogger.log(PrintError, "telegram api error")

    Thread.sleep(1_000)

    // в процессе работы можно изменять поведение Supervisor
    // теперь будет печататься только IgnoreHandler с уровнем Error
    supervisor.after = EmptySolver()

    logger.log(PrintInfo, "result: ${request.get(supervisor)}")

    Thread.sleep(1_000)

    supervisor.before = LoggerSolver(logger, "started ", PrintDebug)
    supervisor.after = LoggerSolver(logger, "finished", PrintDebug)

    request.run(supervisor)

    if (dataBase.getAllCompanies().toList().isEmpty()) {
        DefaultCompanies.entries.map { dataBase.addCompany(it.displayName) }
    }
    val telegramBot = TelegramBot(supervisor, telegramApiLogger, dataBase)
    telegramBot.start()
    Thread.sleep(30_000)
}