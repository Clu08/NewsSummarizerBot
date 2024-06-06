package prod.prog.request.source.database

import prod.prog.actionProperties.contextFactory.DatabaseAction
import prod.prog.dataTypes.Company
import prod.prog.request.source.Source
import prod.prog.service.database.DatabaseService

class CompaniesSource(private val database: DatabaseService) : Source<Iterable<Company>>() {
    init {
        addContext(DatabaseAction(database.name()))
    }

    override fun invoke(t: Unit): Iterable<Company> =
        database.getAllCompanies()

    override fun message() = "CompaniesSourceDB"
}