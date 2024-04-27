package prod.prog.actionProperties.print

interface PrintWarning : PrintError {
    companion object {
        operator fun invoke() = object : PrintWarning {
            override fun message(): String = "warning"
        }
    }
}

