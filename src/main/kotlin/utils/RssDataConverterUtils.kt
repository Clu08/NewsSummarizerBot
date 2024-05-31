package prod.prog.utils

import com.prof18.rssparser.model.RssItem
import prod.prog.dataTypes.NewsPiece

object RssDataConverterUtils {
    fun convertRssItemToNewsPiece(rssItem: RssItem): NewsPiece? {
        // skip item without main data
        if (rssItem.link == null || rssItem.title == null || rssItem.content == null) {
            return null
        }

        return NewsPiece(
            link = rssItem.link!!,
            title = rssItem.title!!,
            text = rssItem.content!!,
            categories = rssItem.categories,
        )
    }
}