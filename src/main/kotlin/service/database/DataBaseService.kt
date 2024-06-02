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

class DataBaseService(private val dataBaseImpl: DataBaseImpl) : DataBaseServiceMethods {
    init {
        Database.connect(
            url = databaseURL(),
            driver = "org.h2.Driver"
        )
        transaction {
            SchemaUtils.createMissingTablesAndColumns(CompanyTable, NewsPieceTable, NewsSummaryTable)
        }
    }

    override fun getCompanyByName(name: String): Company? =
        transaction { dataBaseImpl.getCompanyByName(name) }

    override fun getNewsPieceByLink(link: String): NewsPiece? =
        transaction { dataBaseImpl.getNewsPieceByLink(link) }

    override fun getNewsSummariesByCompany(company: Company): Iterable<NewsSummary> =
        transaction { dataBaseImpl.getNewsSummariesByCompany(company) }

    override fun getNewsSummariesByNewsPiece(newsPiece: NewsPiece): Iterable<NewsSummary> =
        transaction { dataBaseImpl.getNewsSummariesByNewsPiece(newsPiece) }

    override fun getNewsPiecesByCompany(company: Company): Iterable<NewsPiece> =
        transaction { dataBaseImpl.getNewsPiecesByCompany(company) }

    override fun addCompany(name: String) =
        transaction { dataBaseImpl.addCompany(name) }

    override fun addNewsPiece(link: String, text: String) =
        transaction { dataBaseImpl.addNewsPiece(link, text) }

    override fun addNewsSummary(company: Company, newsPiece: NewsPiece, summary: String) =
        transaction { dataBaseImpl.addNewsSummary(company, newsPiece, summary) }

    override fun databaseURL(): String = dataBaseImpl.databaseURL()

    override fun name() = dataBaseImpl.name()
}