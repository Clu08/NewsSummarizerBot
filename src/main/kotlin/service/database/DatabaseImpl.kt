package prod.prog.service.database

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.NewsSummary
import prod.prog.service.database.entity.CompanyEntity
import prod.prog.service.database.entity.NewsPieceEntity
import prod.prog.service.database.entity.NewsSummaryEntity
import prod.prog.service.database.table.CompanyTable
import prod.prog.service.database.table.NewsPieceTable

class DatabaseImpl(private val databaseURL: DatabaseURL) : DatabaseServiceMethods {
    override fun getCompanyByName(name: String): Company? =
        CompanyEntity
            .find(CompanyTable.name eq name)
            .map(CompanyEntity::toCompany)
            .firstOrNull()

    override fun getNewsPieceByLink(link: String): NewsPiece? =
        NewsPieceEntity
            .find(NewsPieceTable.link eq link)
            .map(NewsPieceEntity::toNewsPiece)
            .firstOrNull()

    override fun getNewsSummariesByCompany(company: Company): Iterable<NewsSummary> =
        NewsSummaryEntity
            .all()
            .filter { it.toNewsSummary().company.name == company.name }
            .map(NewsSummaryEntity::toNewsSummary)

    override fun getNewsSummariesByNewsPiece(newsPiece: NewsPiece): Iterable<NewsSummary> =
        NewsSummaryEntity
            .all()
            .filter { it.toNewsSummary().newsPiece.link == newsPiece.link }
            .map(NewsSummaryEntity::toNewsSummary)

    override fun getNewsPiecesByCompany(company: Company) =
        NewsSummaryEntity
            .all()
            .filter { it.toNewsSummary().company.name == company.name }
            .map { it.toNewsSummary().newsPiece }

    override fun addCompany(name: String) {
        CompanyEntity.new {
            this.name = name
        }
    }

    override fun addNewsPiece(link: String, title: String, text: String, categories: List<String>) {
        NewsPieceEntity.new {
            this.link = link
            this.title = title
            this.text = text
            this.categories = categories
        }
    }

    private fun getCompanyEntity(company: Company) =
        CompanyEntity.find(CompanyTable.name eq company.name).firstOrNull()

    private fun getNewsPieceEntity(newsPiece: NewsPiece) =
        NewsPieceEntity.find(NewsPieceTable.link eq newsPiece.link).firstOrNull()

    @Throws(IllegalArgumentException::class)
    override fun addNewsSummary(company: Company, newsPiece: NewsPiece, summary: String) {
        NewsSummaryEntity.new {
            this.company = getCompanyEntity(company) ?: throw IllegalArgumentException()
            this.newsPiece = getNewsPieceEntity(newsPiece) ?: throw IllegalArgumentException()
            this.summary = summary
        }
    }

    override fun databaseURL() = databaseURL.databaseURL

    override fun name() = "Database(${databaseURL.databaseURL})"
}