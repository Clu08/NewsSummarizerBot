package prod.prog.dataTypes.rss

import org.w3c.dom.Element
import prod.prog.dataTypes.NewsPiece
import java.net.URI
import java.net.URL

sealed class RssNewsLink(val sourceUrl: URL) {
    constructor(sourceUrlString: String) : this(URI(sourceUrlString).toURL())

    abstract fun newsParser(newsData: Element): NewsPiece
}
