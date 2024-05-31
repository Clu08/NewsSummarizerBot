package prod.prog.service.rss

import com.prof18.rssparser.RssParser
import kotlinx.coroutines.runBlocking
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.rss.RssNewsLink
import prod.prog.service.newsFilter.NewsFilterService
import prod.prog.utils.RssDataConverterUtils.convertRssItemToNewsPiece

class RssServiceImpl(
    private val newsFilterService: NewsFilterService,
    private val rssParser: RssParser,
) : RssService {

    override fun getNewsByCompany(company: Company, rssNewsLinks: List<RssNewsLink>): List<NewsPiece> {
        return runBlocking {
            rssNewsLinks
                .map { fetchNewsFromRssSource(it) }
                .flatten()
                .filter { news -> newsFilterService.isNewsContainsInfoAboutCompany(news, company) }
        }
    }

    private suspend fun fetchNewsFromRssSource(rssNewsLink: RssNewsLink): List<NewsPiece> = rssParser
        .getRssChannel(rssNewsLink.sourceUrl)
        .items
        .mapNotNull(::convertRssItemToNewsPiece)

    override fun name(): String = "RssServiceImpl"
}