package prod.prog.service.database

import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.NewsSummary
import prod.prog.service.Service

interface DataBaseServiceMethods : Service {
    fun getCompanyByName(name: String): Company?
    fun getNewsPieceByLink(link: String): NewsPiece?
    fun getNewsSummariesByCompany(company: Company): Iterable<NewsSummary>
    fun getNewsSummariesByNewsPiece(newsPiece: NewsPiece): Iterable<NewsSummary>
    fun getNewsPiecesByCompany(company: Company): Iterable<NewsPiece>

    fun addCompany(name: String)
    fun addNewsPiece(link: String, text: String)
    fun addNewsSummary(company: Company, newsPiece: NewsPiece, summary: String)

    fun databaseURL(): String
}