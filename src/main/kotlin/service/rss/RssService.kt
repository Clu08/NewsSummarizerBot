package prod.prog.service.rss

import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.service.Service

interface RssService : Service {
//    todo functions to work with rss

    fun getNewsByCompany(company: Company) : List<NewsPiece>
}