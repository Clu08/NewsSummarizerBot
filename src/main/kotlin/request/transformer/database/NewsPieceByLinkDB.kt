package prod.prog.request.transformer.database

import prod.prog.actionProperties.contextFactory.DatabaseAction
import prod.prog.dataTypes.NewsPiece
import prod.prog.request.transformer.Transformer
import prod.prog.service.database.DatabaseService

class NewsPieceByLinkDB(private val database: DatabaseService) :
    Transformer<String, NewsPiece>() {
    init {
        addContext(DatabaseAction(database.name()))
    }

    override fun invoke(t: String): NewsPiece =
        database.getNewsPieceByLink(t)!!

    override fun message() = "NewsPieceByLinkDB"
}