package prod.prog.service.rss

import org.w3c.dom.Element
import prod.prog.actionProperties.contextFactory.print.PrintWarning
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.rss.RssNewsLink
import prod.prog.service.logger.LoggerService
import prod.prog.service.newsFilter.NewsFilterService
import javax.xml.parsers.DocumentBuilder

class RssServiceImpl(
    private val logger: LoggerService,
    private val newsFilterService: NewsFilterService,
    private val documentBuilder: DocumentBuilder,
) : RssService {

    override fun getNewsByCompany(
        companyList: List<Company>,
        rssNewsLinks: List<RssNewsLink>,
    ): List<Pair<Company, NewsPiece>> =
        rssNewsLinks
            .map { link ->
                val newsList = fetchNewsFromRssSource(link)

                companyList.map { company ->
                    newsList.filter { news ->
                        newsFilterService.isNewsContainsInfoAboutCompany(news, company)
                    }.map { it -> Pair(company, it) }
                }
            }
            .flatten()
            .flatten()

    private fun fetchNewsFromRssSource(rssNewsLink: RssNewsLink): List<NewsPiece> {
        return try {
            rssNewsLink.sourceUrl.openStream().use {
                val sourceData = documentBuilder.parse(it).getElementsByTagName("item")
                return@use (0 until sourceData.length)
                    .map { index -> sourceData.item(index) }
                    .filterIsInstance<Element>()
                    .map { newsItem -> rssNewsLink.newsParser(newsItem) }
            }
        } catch (t: Throwable) {
            logger.log(PrintWarning, "fetchNewsFromRssSource failed ($rssNewsLink): $t")
            listOf()
        }
    }

    override fun name(): String = "RssServiceImpl"
}