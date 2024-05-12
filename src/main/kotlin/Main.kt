package prod.prog

import prod.prog.actionProperties.contextFactory.print.PrintDebug
import prod.prog.actionProperties.contextFactory.print.PrintError
import prod.prog.actionProperties.contextFactory.print.PrintInfo
import prod.prog.request.Request
import prod.prog.request.RequestContext
import prod.prog.request.resultHandler.IgnoreErrorHandler
import prod.prog.request.resultHandler.IgnoreHandler
import prod.prog.request.source.ConstantSource
import prod.prog.request.transformer.IdTransformer
import prod.prog.service.logger.ConsoleLogger
import prod.prog.service.manager.TelegramBot
import prod.prog.service.supervisor.Supervisor
import prod.prog.service.supervisor.solver.EmptySolver
import prod.prog.service.supervisor.solver.actionSolver.LoggerSolver
import prod.prog.service.supervisor.solver.requestSolver.SetUniqueIdSolver

fun main() {
    val logger = ConsoleLogger()

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

    Thread.sleep(1_000)

    // в процессе работы можно изменять поведение Supervisor
    // теперь будет печататься только IgnoreHandler с уровнем Error
    supervisor.after = EmptySolver()

    logger.log(PrintInfo, "result: ${request.get(supervisor)}")

    Thread.sleep(1_000)

    supervisor.before = LoggerSolver(logger, "started ", PrintDebug)
    supervisor.after = LoggerSolver(logger, "finished", PrintDebug)

    val telegramBot = TelegramBot(supervisor, logger)
    telegramBot.start()
}