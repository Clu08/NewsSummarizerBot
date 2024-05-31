package prod.prog.dataTypes.rss

/**
 * Allowed rss source to use.
 * Prevents usage of unwanted urls and makes sources a singleton classes.
 */
enum class AvailableRssSources(val rssNewsLink: RssNewsLink) {
    LENTA(LentaRssNewsLink()),
    RBK(RbkRssNewsLink())
}

private class LentaRssNewsLink : RssNewsLink("https://lenta.ru/rss/news")

private class RbkRssNewsLink : RssNewsLink("https://rssexport.rbc.ru/rbcnews/news/30/full.rss")
