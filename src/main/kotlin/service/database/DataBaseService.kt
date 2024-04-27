package prod.prog.service.database

import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.NewsSummary

interface DataBaseService {
//    todo functions to work with database

    fun getCompanyByName(name: String): Company?
    fun getNewsPieceByLink(link: String): NewsPiece?
    fun getNewsSummariesByCompany(company: Company): Iterable<NewsSummary>
    fun getNewsSummariesByNewsPiece(newsPiece: NewsPiece): Iterable<NewsSummary>
}