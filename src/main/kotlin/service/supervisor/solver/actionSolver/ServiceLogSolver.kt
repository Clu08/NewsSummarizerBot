package prod.prog.service.supervisor.solver.actionSolver

import io.sentry.Sentry
import prod.prog.actionProperties.Context
import prod.prog.actionProperties.contextFactory.DatabaseAction
import prod.prog.actionProperties.contextFactory.GptAction
import prod.prog.actionProperties.contextFactory.RssAction
import prod.prog.service.supervisor.solver.IdSolver

class ServiceLogSolver(private val prefix: String) : IdSolver<Context> {
    override fun solve(t: Context) {
        when {
            t.has(DatabaseAction()) -> {
                val transaction = Sentry.startTransaction("$prefix database()", t[DatabaseAction()].toString())
                transaction.finish()
            }
        }
        when {
            t.has(RssAction()) -> {
                val transaction = Sentry.startTransaction("$prefix rss()", t[RssAction()].toString())
                transaction.finish()
            }
        }
        when {
            t.has(GptAction()) -> {
                val transaction = Sentry.startTransaction("$prefix gpt()", t[GptAction()].toString())
                transaction.finish()
            }
        }
    }
}