package prod.prog.request.resultHandler.database

import prod.prog.actionProperties.contextFactory.DatabaseAction
import prod.prog.dataTypes.NewsSummary
import prod.prog.request.resultHandler.ResultHandler
import prod.prog.service.database.DatabaseService

class AddNewsSummaryDBHandler(private val database: DatabaseService) : ResultHandler<NewsSummary>() {
    init {
        addContext(DatabaseAction(database.name()))
    }

    override fun invoke(t: NewsSummary) {
        database.addNewsSummary(t.company, t.newsPiece, t.summary)
    }

    override fun message() = "AddNewsSummaryDBHandler"

}