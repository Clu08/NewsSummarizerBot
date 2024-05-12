package prod.prog.request.source.database

import prod.prog.actionProperties.contextFactory.DataBaseAction
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.request.source.Source
import prod.prog.service.database.DataBaseService

class NewsPiecesByCompanySource(private val dataBase: DataBaseService, private val company: Company) :
    Source<Iterable<NewsPiece>>() {
    init {
        addContext(DataBaseAction(dataBase.name()))
    }

    override fun getSource(): Iterable<NewsPiece> =
        dataBase.getNewsPiecesByCompany(company)

    override fun message() = "NewsPiecesByCompanyDB(${company.name})"
}