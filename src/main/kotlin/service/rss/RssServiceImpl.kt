package prod.prog.service.rss

import com.prof18.rssparser.RssParser
import kotlinx.coroutines.coroutineScope
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.rss.RssSource
import prod.prog.service.newsFilter.NewsFilterService
import prod.prog.utils.RssDataConverterUtils.convertRssItemToNewsPiece
import prod.prog.utils.parallelMap

class RssServiceImpl(
    private val newsFilterService: NewsFilterService,
    private val rssParser: RssParser,
) : RssService {

    override suspend fun fetchNewsFromRssSource(rssSource: RssSource): List<NewsPiece> = coroutineScope {
        return@coroutineScope rssParser
            .getRssChannel(rssSource.sourceUrl)
            .items
            .parallelMap(::convertRssItemToNewsPiece)
            .filterNotNull()
    }

    override suspend fun getNewsByCompany(company: Company, rssSources: List<RssSource>): List<NewsPiece> =
        coroutineScope {
            return@coroutineScope rssSources
                .parallelMap { fetchNewsFromRssSource(it) }
                .flatten()
                .filter { news -> newsFilterService.isNewsContainsInfoAboutCompany(news, company) }
        }

    override fun name(): String = "RssServiceImpl"
}