package prod.prog.actionProperties.contextFactory.print

fun interface PrintTrace : PrintFatal {
    override fun key() = PrintError()

    companion object : PrintError {
        operator fun invoke() = "PrintTrace"
        override fun value() = "no message"
    }
}