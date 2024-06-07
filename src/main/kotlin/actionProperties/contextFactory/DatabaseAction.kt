package prod.prog.actionProperties.contextFactory

open class DatabaseAction(private val databaseName: String) : PropertyAdderFactory {
    override fun key() = DatabaseAction()
    override fun value() = databaseName

    companion object : DatabaseAction("no name") {
        operator fun invoke() = "UsesDatabase"
    }
}