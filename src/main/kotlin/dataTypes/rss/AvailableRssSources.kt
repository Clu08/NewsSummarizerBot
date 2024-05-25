package prod.prog.dataTypes.rss

/**
 * Allowed rss source to use.
 * Prevents usage of unwanted urls and makes sources a singleton classes.
 */
enum class AvailableRssSources(val rssSource: RssSource) {
    LENTA(LentaRssSource()),
    RBK(RbkRssSource())
}

private class LentaRssSource : RssSource("https://lenta.ru/rss/news")

private class RbkRssSource : RssSource("https://rssexport.rbc.ru/rbcnews/news/30/full.rss")
