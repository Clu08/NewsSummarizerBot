package prod.prog.service.rss

import org.w3c.dom.Element
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.rss.RssNewsLink
import prod.prog.service.newsFilter.NewsFilterService
import javax.xml.parsers.DocumentBuilder

class RssServiceImpl(
    private val newsFilterService: NewsFilterService,
    private val documentBuilder: DocumentBuilder,
) : RssService {

    override fun getNewsByCompany(company: Company, rssNewsLinks: List<RssNewsLink>): List<NewsPiece> {
        return rssNewsLinks
            .map { fetchNewsFromRssSource(it) }
            .flatten()
            .filter { news -> newsFilterService.isNewsContainsInfoAboutCompany(news, company) }
    }

    private fun fetchNewsFromRssSource(rssNewsLink: RssNewsLink): List<NewsPiece> =
        rssNewsLink.sourceUrl.openStream().use {
            val sourceData = documentBuilder.parse(it).getElementsByTagName("item")
            return@use (0 until sourceData.length)
                .map { index -> sourceData.item(index) }
                .filterIsInstance<Element>()
                .map { newsItem -> rssNewsLink.newsParser(newsItem) }
        }

    override fun name(): String = "RssServiceImpl"
}