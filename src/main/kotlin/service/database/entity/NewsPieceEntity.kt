package prod.prog.service.database.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import prod.prog.dataTypes.NewsPiece
import prod.prog.service.database.table.NewsPieceTable

class NewsPieceEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NewsPieceEntity>(NewsPieceTable)

    var link by NewsPieceTable.link
    var title by NewsPieceTable.title
    var text by NewsPieceTable.text
    var categories by NewsPieceTable.categories

    fun toNewsPiece() = NewsPiece(link, title, text, categories)
}