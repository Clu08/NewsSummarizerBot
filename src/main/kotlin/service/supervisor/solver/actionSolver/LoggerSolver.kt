package prod.prog.service.supervisor.solver.actionSolver

import prod.prog.actionProperties.Action
import prod.prog.actionProperties.ActionWithContext
import prod.prog.actionProperties.print.*
import prod.prog.service.logger.LoggerService
import prod.prog.service.supervisor.solver.IdSolver

class LoggerSolver(
    private val logger: LoggerService,
    private val prefix: String,
    private val logLevel: PrintFatal,
) :
    IdSolver<ActionWithContext<out Action>> {
    override fun solve(t: ActionWithContext<out Action>) {
        val context = t.context
        val action = t.action

        val actionMessage = when (action) {
            is PrintFatal -> action.message()
            else -> "no message"
        }
        val fullMessage = "id ${context.id}\t${context.createdBy}\t\t$prefix\t$actionMessage"

        when (action) {
            is PrintDebug -> when (logLevel) {
                is PrintDebug -> logger.log(PrintDebug(), fullMessage)
            }

            is PrintInfo -> when (logLevel) {
                is PrintInfo -> logger.log(PrintInfo(), fullMessage)
            }

            is PrintWarning -> when (logLevel) {
                is PrintWarning -> logger.log(PrintWarning(), fullMessage)
            }

            is PrintError -> when (logLevel) {
                is PrintError -> logger.log(PrintError(), fullMessage)
            }

            is PrintFatal -> logger.log(PrintFatal(), fullMessage)
        }
    }
}