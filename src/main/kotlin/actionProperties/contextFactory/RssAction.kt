package prod.prog.actionProperties.contextFactory

open class RssAction(private val rssName: String) : PropertyAdderFactory {
    override fun key() = RssAction()
    override fun value() = rssName

    companion object : RssAction("no name") {
        operator fun invoke() = "UsesRss"
    }
}