package prod.prog.actionProperties.contextFactory.print

fun interface PrintWarning : PrintError {
    override fun key() = PrintWarning()

    companion object : PrintWarning {
        operator fun invoke() = "PrintWarning"
        override fun value() = "no message"
    }
}

