package prod.prog.actionProperties.contextFactory.print

fun interface PrintDebug : PrintInfo {
    override fun key() = PrintDebug()

    companion object : PrintDebug {
        operator fun invoke() = "PrintDebug"
        override fun value() = "no message"
    }
}