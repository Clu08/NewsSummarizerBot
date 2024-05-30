package prod.prog.service.rss

import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.rss.RssNewsLink
import prod.prog.service.Service

/**
 * Fetches data from rss sources and converts it to internal representation
 */
interface RssService : Service {
    fun getNewsByCompany(company: Company, rssNewsLinks: List<RssNewsLink>): List<NewsPiece>
}