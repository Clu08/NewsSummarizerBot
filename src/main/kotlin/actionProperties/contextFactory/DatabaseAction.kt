package prod.prog.actionProperties.contextFactory

class DatabaseAction(private val dataBaseName: String = "no name") : PropertyAdderFactory {
    override fun key() = "usesDatabase"
    override fun value() = dataBaseName
}