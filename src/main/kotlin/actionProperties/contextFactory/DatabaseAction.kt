package prod.prog.actionProperties.contextFactory

class DatabaseAction(private val databaseName: String = "no name") : PropertyAdderFactory {
    override fun key() = "usesDatabase"
    override fun value() = databaseName
}