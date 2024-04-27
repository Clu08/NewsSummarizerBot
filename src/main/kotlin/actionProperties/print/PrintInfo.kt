package prod.prog.actionProperties.print

interface PrintInfo : PrintWarning {
    companion object {
        operator fun invoke() = object : PrintInfo {
            override fun message(): String = "info"
        }
    }
}