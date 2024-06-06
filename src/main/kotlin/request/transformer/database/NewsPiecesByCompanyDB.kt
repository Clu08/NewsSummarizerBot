package prod.prog.request.transformer.database

import prod.prog.actionProperties.contextFactory.DatabaseAction
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.request.transformer.Transformer
import prod.prog.service.database.DatabaseService

class NewsPiecesByCompanyDB(private val database: DatabaseService) :
    Transformer<Company, Iterable<NewsPiece>>() {
    init {
        addContext(DatabaseAction(database.name()))
    }

    override fun invoke(t: Company): Iterable<NewsPiece> =
        database.getNewsPiecesByCompany(t)

    override fun message() = "NewsPiecesByCompanyDB"
}