package prod.prog.common

import com.prof18.rssparser.model.RssItem

fun randomId() = (Long.MIN_VALUE..Long.MAX_VALUE).random()

fun mockedRssItem(link: String?, title: String?, content: String?): RssItem {
    return RssItem(null, title, null, link, null, null, content, null, null, null, null, null, listOf(), null, null)
}