package prod.prog.service.supervisor.solver.actionSolver

import prod.prog.actionProperties.Action
import prod.prog.actionProperties.print.*
import prod.prog.service.logger.LoggerService
import prod.prog.service.supervisor.solver.IdSolver

class LoggerSolver(private val logger: LoggerService, private val prefix: String, private val logLevel: PrintFatal) :
    IdSolver<Action> {
    override fun solve(t: Action) {
        val actionMessage = when (t) {
            is PrintFatal -> t.message()
            else -> "no message"
        }
        val fullMessage = "id ${t.id}\t${t.createdBy}\t\t$prefix\t$actionMessage"

        when (t) {
            is PrintDebug -> when (logLevel) {
                is PrintDebug -> logger.log(PrintDebug, fullMessage)
            }

            is PrintInfo -> when (logLevel) {
                is PrintInfo -> logger.log(PrintInfo, fullMessage)
            }

            is PrintWarning -> when (logLevel) {
                is PrintWarning -> logger.log(PrintWarning, fullMessage)
            }

            is PrintError -> when (logLevel) {
                is PrintError -> logger.log(PrintError, fullMessage)
            }

            is PrintFatal -> logger.log(PrintFatal, fullMessage)
        }
    }
}