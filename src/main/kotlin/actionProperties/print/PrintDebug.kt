package prod.prog.actionProperties.print

import prod.prog.actionProperties.ActionClass

interface PrintDebug : PrintInfo {
    companion object : ActionClass(), PrintDebug {
        override fun message(): String = "debug"
    }
}