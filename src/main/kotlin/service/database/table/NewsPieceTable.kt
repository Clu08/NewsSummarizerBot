package prod.prog.service.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object NewsPieceTable : IntIdTable("newsPieces") {
    val link = varchar("link", 100).uniqueIndex()
    val title = largeText("title")
    val text = largeText("text")
    val categories = array<String>("categories")
}