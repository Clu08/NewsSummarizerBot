package prod.prog.actionProperties.contextFactory

open class GptAction(private val gptName: String) : PropertyAdderFactory {
    override fun key() = DatabaseAction()
    override fun value() = gptName

    companion object : GptAction("no name") {
        operator fun invoke() = "UsesGPT"
    }
}