package prod.prog.request.source.database

import prod.prog.dataTypes.Company
import prod.prog.request.source.Source
import prod.prog.service.database.DataBaseService

class CompanySource(private val dataBase: DataBaseService, private val name: String) : Source<Company> {
    override fun getSource(): Company =
        dataBase.getCompanyByName(name)!!

    override fun message() = "CompanySourceDB($name)"
}