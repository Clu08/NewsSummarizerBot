package prod.prog.request.source.database

import prod.prog.actionProperties.contextFactory.DatabaseAction
import prod.prog.dataTypes.NewsPiece
import prod.prog.request.source.Source
import prod.prog.service.database.DatabaseService

class NewsPieceSource(private val dataBase: DatabaseService, private val link: String) : Source<NewsPiece>() {
    init {
        addContext(DatabaseAction(dataBase.name()))
    }

    override fun getSource(): NewsPiece =
        dataBase.getNewsPieceByLink(link)!!

    override fun message() = "NewsPieceSourceDB($link)"
}