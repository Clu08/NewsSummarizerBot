package prod.prog.service.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object NewsPieceTable : IntIdTable("newsPieces") {
    val link = varchar("link", 50).uniqueIndex()
    val text = largeText("text")
}