package prod.prog.request.source.database

import prod.prog.actionProperties.contextFactory.DatabaseAction
import prod.prog.dataTypes.Company
import prod.prog.request.source.Source
import prod.prog.service.database.DatabaseService

class CompaniesSourceDB(private val database: DatabaseService) : Source<List<Company>>() {
    init {
        addContext(DatabaseAction(database.name()))
    }

    override fun invoke(t: Unit): List<Company> =
        database.getAllCompanies()

    override fun message() = "CompaniesSourceDB"
}