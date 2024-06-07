package prod.prog.service.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.NewsSummary
import prod.prog.service.database.table.CompanyTable
import prod.prog.service.database.table.NewsPieceTable
import prod.prog.service.database.table.NewsSummaryTable

class DatabaseService(private val databaseImpl: DatabaseImpl) : DatabaseServiceMethods {
    init {
        Database.connect(
            url = databaseURL(),
            driver = "org.h2.Driver"
        )
        transaction {
            SchemaUtils.createMissingTablesAndColumns(CompanyTable, NewsPieceTable, NewsSummaryTable)
        }
    }

    override fun getAllCompanies(): List<Company> =
        transaction { databaseImpl.getAllCompanies() }

    override fun getCompanyByName(name: String): Company? =
        transaction { databaseImpl.getCompanyByName(name) }

    override fun getNewsPieceByLink(link: String): NewsPiece? =
        transaction { databaseImpl.getNewsPieceByLink(link) }

    override fun getNewsSummariesByCompany(company: Company): List<NewsSummary> =
        transaction { databaseImpl.getNewsSummariesByCompany(company) }

    override fun getNewsSummariesByNewsPiece(newsPiece: NewsPiece): List<NewsSummary> =
        transaction { databaseImpl.getNewsSummariesByNewsPiece(newsPiece) }

    override fun getNewsPiecesByCompany(company: Company): List<NewsPiece> =
        transaction { databaseImpl.getNewsPiecesByCompany(company) }

    override fun addCompany(name: String) =
        transaction { databaseImpl.addCompany(name) }

    override fun addNewsPiece(link: String, title: String, text: String, categories: List<String>) =
        transaction { databaseImpl.addNewsPiece(link, title, text, categories) }

    override fun addNewsSummary(company: Company, newsPiece: NewsPiece, summary: String) =
        transaction { databaseImpl.addNewsSummary(company, newsPiece, summary) }

    override fun databaseURL(): String = databaseImpl.databaseURL()

    override fun name() = databaseImpl.name()
}