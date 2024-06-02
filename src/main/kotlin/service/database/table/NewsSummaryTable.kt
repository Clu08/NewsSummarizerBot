package prod.prog.service.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object NewsSummaryTable : IntIdTable("newsSummaries") {
    val companyId = reference("companyId", CompanyTable)
    val newsPieceId = reference("newsPieceId", NewsPieceTable)
    val summary = largeText("summary")
}