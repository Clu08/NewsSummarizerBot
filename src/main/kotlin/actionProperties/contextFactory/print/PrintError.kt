package prod.prog.actionProperties.contextFactory.print

fun interface PrintError : PrintFatal {
    override fun key() = PrintError()

    companion object : PrintError {
        operator fun invoke() = "PrintError"
        override fun value() = "no message"
    }
}