package prod.prog.service.rss

import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.RssSource
import prod.prog.service.Service

/**
 * Fetches data from rss sources and converts it to internal representation
 */
interface RssService : Service {
    suspend fun fetchNewsFromRssSource(rssSource: RssSource): List<NewsPiece>

    suspend fun getNewsByCompany(company: Company, rssSources: List<RssSource>): List<NewsPiece>
}