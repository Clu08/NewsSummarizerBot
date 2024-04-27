package prod.prog.actionProperties.print

interface PrintDebug : PrintInfo {
    companion object {
        operator fun invoke() = object : PrintDebug {
            override fun message(): String = "debug"
        }
    }
}