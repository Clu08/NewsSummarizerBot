package prod.prog.actionProperties.print

interface PrintError : PrintFatal {
    companion object {
        operator fun invoke() = object : PrintError {
            override fun message(): String = "error"
        }
    }
}