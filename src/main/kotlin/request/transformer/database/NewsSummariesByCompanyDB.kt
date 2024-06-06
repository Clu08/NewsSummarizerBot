package prod.prog.request.transformer.database

import prod.prog.actionProperties.contextFactory.DatabaseAction
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsSummary
import prod.prog.request.transformer.Transformer
import prod.prog.service.database.DatabaseService

class NewsSummariesByCompanyDB(private val database: DatabaseService) :
    Transformer<Company, Iterable<NewsSummary>>() {
    init {
        addContext(DatabaseAction(database.name()))
    }

    override fun invoke(t: Company): Iterable<NewsSummary> =
        database.getNewsSummariesByCompany(t)

    override fun message() = "NewsSummariesByCompanyDB"
}