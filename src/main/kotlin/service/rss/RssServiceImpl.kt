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
        println("fetchNewsFromRssSource $rssNewsLink")
        return rssNewsLink.sourceUrl.openStream().use {
            val sourceData = documentBuilder.parse(it).getElementsByTagName("item")
            return@use (0 until sourceData.length)
                .map { index -> sourceData.item(index) }
                .filterIsInstance<Element>()
                .map { newsItem -> rssNewsLink.newsParser(newsItem) }
        }
    }

    override fun name(): String = "RssServiceImpl"
}