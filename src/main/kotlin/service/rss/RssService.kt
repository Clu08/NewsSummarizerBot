package prod.prog.service.rss

import prod.prog.dataTypes.Company

interface RssService {
//    todo functions to work with rss

    fun getNewsByCompany(company: Company)
}