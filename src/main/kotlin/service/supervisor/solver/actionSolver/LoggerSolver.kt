package prod.prog.service.supervisor.solver.actionSolver

import prod.prog.actionProperties.Context
import prod.prog.actionProperties.contextFactory.print.*
import prod.prog.service.logger.LoggerService
import prod.prog.service.supervisor.solver.IdSolver

class LoggerSolver(
    private val logger: LoggerService,
    private val prefix: String,
    private val logLevel: PrintFatal,
) : IdSolver<Context> {
    override fun solve(t: Context) {
        val fullMessage = "id ${t["id"]}\t${t["createdBy"]}\t\t$prefix\t"

        when {
            t.has(PrintFatal()) -> logger.log(PrintFatal, fullMessage + t[PrintFatal()])
        }
        when {
            t.has(PrintDebug()) -> when (logLevel) {
                is PrintDebug -> logger.log(PrintDebug, fullMessage + t[PrintDebug()])
            }
        }
        when {
            t.has(PrintInfo()) -> when (logLevel) {
                is PrintInfo -> logger.log(PrintInfo, fullMessage + t[PrintInfo()])
            }
        }
        when {
            t.has(PrintWarning()) -> when (logLevel) {
                is PrintWarning -> logger.log(PrintWarning, fullMessage + t[PrintWarning()])
            }
        }
        when {
            t.has(PrintError()) -> when (logLevel) {
                is PrintError -> logger.log(PrintError, fullMessage + t[PrintError()])
            }
        }
    }
}