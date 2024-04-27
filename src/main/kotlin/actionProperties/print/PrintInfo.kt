package prod.prog.actionProperties.print

import prod.prog.actionProperties.ActionClass

interface PrintInfo : PrintWarning {
    companion object : ActionClass(), PrintInfo {
        override fun message(): String = "info"
    }
}