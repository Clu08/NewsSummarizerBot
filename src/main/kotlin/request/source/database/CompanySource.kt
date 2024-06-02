package prod.prog.request.source.database

import prod.prog.actionProperties.contextFactory.DatabaseAction
import prod.prog.dataTypes.Company
import prod.prog.request.source.Source
import prod.prog.service.database.DatabaseService

class CompanySource(private val dataBase: DatabaseService, private val name: String) : Source<Company>() {
    init {
        addContext(DatabaseAction(dataBase.name()))
    }

    override fun getSource(): Company =
        dataBase.getCompanyByName(name)!!

    override fun message() = "CompanySourceDB($name)"
}