package prod.prog.service.rss
import prod.prog.dataTypes.Company
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Element
import prod.prog.actionProperties.contextFactory.print.PrintInfo
import prod.prog.dataTypes.NewsPiece
import prod.prog.service.logger.ConsoleLogger

class NewsRss() : RssService {
    override fun getNewsByCompany(company: Company): List<NewsPiece> {
        val logger = ConsoleLogger()
        val result = mutableListOf<NewsPiece>()

        val keyword = company.name
        val feeds = FeedsConfig.urlFeeds

        feeds.forEach { (source, url) ->
            logger.log(PrintInfo, "Checking $source for news related to '$keyword'...")
            val document = URL(url).openStream().use {
                DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(it)
            }
            val items = document.getElementsByTagName("item")
            for (i in 0 until items.length) {
                val item = items.item(i) as Element
                val title = item.getElementsByTagName("title").item(0)?.textContent
                val link = item.getElementsByTagName("link").item(0)?.textContent
                val description = item.getElementsByTagName("description").item(0)?.textContent

                if (title != null && link != null && description != null) {
                    if (title.contains(keyword, ignoreCase=true) || description.contains(keyword, ignoreCase = true)) {
                        result.add(NewsPiece(link, title))
                        logger.log(PrintInfo, "Found news related to '$keyword' in $source")
                    }
                }

            }
        }
        return result
    }

    override fun name() = "Company"
}