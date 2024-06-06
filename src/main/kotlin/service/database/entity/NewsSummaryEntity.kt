package prod.prog.service.database.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import prod.prog.dataTypes.NewsSummary
import prod.prog.service.database.table.NewsSummaryTable

class NewsSummaryEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NewsSummaryEntity>(NewsSummaryTable)

    var company by CompanyEntity referencedOn NewsSummaryTable.companyId
    var newsPiece by NewsPieceEntity referencedOn NewsSummaryTable.newsPieceId
    var summary by NewsSummaryTable.summary

    fun toNewsSummary() = NewsSummary(
        company.toCompany(),
        newsPiece.toNewsPiece(),
        summary
    )
}