package prod.prog.service.database

import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.NewsSummary
import prod.prog.service.Service

interface DatabaseServiceMethods : Service {
    fun getAllCompanies(): List<Company>
    fun getAllNewsPieces(): List<NewsPiece>
    fun getAllNewsSummaries(): List<NewsSummary>
    fun getCompanyByName(name: String): Company?
    fun getNewsPieceByLink(link: String): NewsPiece?
    fun getNewsSummariesByCompany(company: Company): List<NewsSummary>
    fun getNewsSummariesByNewsPiece(newsPiece: NewsPiece): List<NewsSummary>
    fun getNewsPiecesByCompany(company: Company): List<NewsPiece>

    fun addCompany(name: String)
    fun addNewsPiece(link: String, title: String, text: String, categories: List<String> = listOf())
    fun addNewsSummary(company: Company, newsPiece: NewsPiece, summary: String)

    fun databaseURL(): String
}