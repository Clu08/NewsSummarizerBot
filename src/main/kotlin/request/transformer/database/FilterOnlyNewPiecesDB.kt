package prod.prog.request.transformer.database

import prod.prog.actionProperties.contextFactory.DatabaseAction
import prod.prog.dataTypes.NewsPiece
import prod.prog.request.transformer.Transformer
import prod.prog.service.database.DatabaseService

class FilterOnlyNewPiecesDB(private val database: DatabaseService) : Transformer<List<NewsPiece>, List<NewsPiece>>() {
    init {
        addContext(DatabaseAction(database.name()))
    }

    override fun invoke(t: List<NewsPiece>): List<NewsPiece> =
        t.filterNot { it.link in database.getExistingNewsPieces(t).map(NewsPiece::link) }

    override fun message() = "FilterOnlyNewPiecesDB"
}