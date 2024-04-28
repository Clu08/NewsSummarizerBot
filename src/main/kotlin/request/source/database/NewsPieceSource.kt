package prod.prog.request.source.database

import prod.prog.dataTypes.NewsPiece
import prod.prog.request.source.Source
import prod.prog.service.database.DataBaseService

class NewsPieceSource(private val dataBase: DataBaseService, private val link: String) : Source<NewsPiece> {
    override fun getSource(): NewsPiece =
        dataBase.getNewsPieceByLink(link)!!

    override fun message() = "NewsPieceSourceDB($link)"
}