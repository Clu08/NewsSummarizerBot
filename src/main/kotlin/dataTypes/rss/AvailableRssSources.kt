package prod.prog.dataTypes.rss

import org.w3c.dom.Element
import prod.prog.dataTypes.NewsPiece
import prod.prog.utils.getCharacterDataFromElement

/**
 * Allowed rss source to use.
 * Prevents usage of unwanted urls and makes sources a singleton classes.
 */
enum class AvailableRssSources(val rssNewsLink: RssNewsLink) {
    LENTA(LentaRssNewsLink()),
    RBK(RbkRssNewsLink()),
    KOMMERSANT(KommersantRssNewsLink()),
    RAMBLER(RamblerRssNewsLink()),
    BIGGERPOCKETS(BiggerPocketsRssNewsLink()),
    NYTIMES(NYtimesRssNewsLink()),
}


private class LentaRssNewsLink : RssNewsLink("https://lenta.ru/rss/news") {
    override fun newsParser(newsData: Element): NewsPiece {
        return NewsPiece(
            link = newsData.getElementsByTagName("link").item(0)?.textContent ?: "",
            title = newsData.getElementsByTagName("title").item(0)?.textContent ?: "",
            text = getCharacterDataFromElement(newsData.getElementsByTagName("description").item(0) as? Element),
        )
    }
}

private class RbkRssNewsLink : RssNewsLink("https://rssexport.rbc.ru/rbcnews/news/30/full.rss") {
    override fun newsParser(newsData: Element): NewsPiece {
        return NewsPiece(
            link = newsData.getElementsByTagName("link").item(0)?.textContent ?: "",
            title = newsData.getElementsByTagName("title").item(0)?.textContent ?: "",
            text = newsData.getElementsByTagName("rbc_news:full-text").item(0)?.textContent ?: "",
        )
    }
}


private class RamblerRssNewsLink : RssNewsLink("https://finance.rambler.ru/rss/business/") {
    override fun newsParser(newsData: Element): NewsPiece {
        return NewsPiece(
            link = newsData.getElementsByTagName("link").item(0)?.textContent ?: "",
            title = newsData.getElementsByTagName("title").item(0)?.textContent ?: "",
            text = newsData.getElementsByTagName("description").item(0)?.textContent ?: ""
        )
    }
}

private class KommersantRssNewsLink : RssNewsLink("https://www.kommersant.ru/RSS/news.xml") {
    override fun newsParser(newsData: Element): NewsPiece {
        return NewsPiece(
            link = newsData.getElementsByTagName("link").item(0)?.textContent ?: "",
            title = newsData.getElementsByTagName("title").item(0)?.textContent ?: "",
            text = newsData.getElementsByTagName("description").item(0)?.textContent ?: ""
        )
    }
}

private class BiggerPocketsRssNewsLink : RssNewsLink("https://www.biggerpockets.com/blog/feed") {
    override fun newsParser(newsData: Element): NewsPiece {
        return NewsPiece(
            link = newsData.getElementsByTagName("link").item(0)?.textContent ?: "",
            title = newsData.getElementsByTagName("title").item(0)?.textContent ?: "",
            text = getCharacterDataFromElement(newsData.getElementsByTagName("description").item(0) as? Element),
        )
    }
}

private class NYtimesRssNewsLink : RssNewsLink("https://www.nytimes.com/svc/collections/v1/publish/https://www.nytimes.com/section/world/rss.xml") {
    override fun newsParser(newsData: Element): NewsPiece {
        return NewsPiece(
            link = newsData.getElementsByTagName("link").item(0)?.textContent ?: "",
            title = newsData.getElementsByTagName("title").item(0)?.textContent ?: "",
            text = newsData.getElementsByTagName("rbc_news:full-text").item(0)?.textContent ?: "",
        )
    }
}