package prod.prog.actionProperties.contextFactory.print

fun interface PrintInfo : PrintWarning {
    override fun key() = PrintInfo()

    companion object : PrintInfo {
        operator fun invoke() = "PrintInfo"
        override fun value() = "no message"
    }
}