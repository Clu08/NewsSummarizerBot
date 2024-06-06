package prod.prog.request.transformer.database

import prod.prog.actionProperties.contextFactory.DatabaseAction
import prod.prog.dataTypes.Company
import prod.prog.request.transformer.Transformer
import prod.prog.service.database.DatabaseService

class CompanyByNameDB(private val database: DatabaseService) : Transformer<String, Company>() {
    init {
        addContext(DatabaseAction(database.name()))
    }

    override fun invoke(t: String): Company =
        database.getCompanyByName(t)!!

    override fun message() = "CompanyByNameDB"
}