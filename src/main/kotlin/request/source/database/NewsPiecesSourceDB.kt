package prod.prog.request.source.database

import prod.prog.actionProperties.contextFactory.DatabaseAction
import prod.prog.dataTypes.NewsPiece
import prod.prog.request.source.Source
import prod.prog.service.database.DatabaseService

class NewsPiecesSourceDB(private val database: DatabaseService) : Source<List<NewsPiece>>() {
    init {
        addContext(DatabaseAction(database.name()))
    }

    override fun invoke(t: Unit): List<NewsPiece> =
        database.getAllNewsPieces()

    override fun message() = "NewsPiecesSourceDB"
}