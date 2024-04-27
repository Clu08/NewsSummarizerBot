package prod.prog.actionProperties.print

import prod.prog.actionProperties.ActionClass

interface PrintWarning : PrintError {
    companion object : ActionClass(), PrintWarning {
        override fun message(): String = "warning"
    }
}