package prod.prog.service.database

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.and
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.NewsSummary
import prod.prog.service.database.entity.CompanyEntity
import prod.prog.service.database.entity.NewsPieceEntity
import prod.prog.service.database.entity.NewsSummaryEntity
import prod.prog.service.database.table.CompanyTable
import prod.prog.service.database.table.NewsPieceTable
import prod.prog.service.database.table.NewsSummaryTable

class DatabaseImpl(private val databaseURL: DatabaseURL) : DatabaseServiceMethods {
    override fun getAllCompanies(): List<Company> =
        CompanyEntity
            .all()
            .map(CompanyEntity::toCompany)

    override fun getAllNewsPieces(): List<NewsPiece> =
        NewsPieceEntity
            .all()
            .map(NewsPieceEntity::toNewsPiece)

    override fun getAllNewsSummaries(): List<NewsSummary> =
        NewsSummaryEntity
            .all()
            .map(NewsSummaryEntity::toNewsSummary)

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

    override fun getNewsSummariesByCompany(company: Company): List<NewsSummary> =
        NewsSummaryEntity
            .all()
            .filter { it.toNewsSummary().company.name == company.name }
            .map(NewsSummaryEntity::toNewsSummary)

    override fun getNewsSummariesByNewsPiece(newsPiece: NewsPiece): List<NewsSummary> =
        NewsSummaryEntity
            .all()
            .filter { it.toNewsSummary().newsPiece.link == newsPiece.link }
            .map(NewsSummaryEntity::toNewsSummary)

    override fun getNewsPiecesByCompany(company: Company) =
        NewsSummaryEntity
            .all()
            .filter { it.toNewsSummary().company.name == company.name }
            .map { it.toNewsSummary().newsPiece }

    override fun getExistingNewsPieces(newsPieces: List<NewsPiece>) =
        NewsPieceEntity
            .find(NewsPieceTable.link inList newsPieces.map { it.link })
            .map(NewsPieceEntity::toNewsPiece)

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

    private fun getCompanyEntity(company: Company): CompanyEntity =
        CompanyEntity.find(CompanyTable.name eq company.name).firstOrNull() ?: run {
            addCompany(company.name)
            CompanyEntity.find(CompanyTable.name eq company.name).firstOrNull()
                ?: throw InternalError("DatabaseImpl.addNewsSummary no $company")
        }

    private fun getNewsPieceEntity(newsPiece: NewsPiece) =
        NewsPieceEntity.find(NewsPieceTable.link eq newsPiece.link).firstOrNull() ?: run {
            addNewsPiece(newsPiece.link, newsPiece.title, newsPiece.text, newsPiece.categories)
            NewsPieceEntity.find(NewsPieceTable.link eq newsPiece.link).firstOrNull()
                ?: throw InternalError("DatabaseImpl.addNewsSummary no $newsPiece")
        }

    override fun addNewsSummary(company: Company, newsPiece: NewsPiece, summary: String) {
        val companyEntity = getCompanyEntity(company)
        val newsPieceEntity = getNewsPieceEntity(newsPiece)

        NewsSummaryEntity.find(
            (NewsSummaryTable.companyId eq companyEntity.id)
                    and (NewsSummaryTable.newsPieceId eq newsPieceEntity.id)
        ).firstOrNull() ?: NewsSummaryEntity.new {
            this.company = companyEntity
            this.newsPiece = newsPieceEntity
            this.summary = summary
        }
    }

    override fun databaseURL() = databaseURL.databaseURL

    override fun name() = "Database(${databaseURL.databaseURL})"
}