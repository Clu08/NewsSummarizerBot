package prod.prog.actionProperties.print

import prod.prog.actionProperties.ActionClass

interface PrintError : PrintFatal {
    companion object : ActionClass(), PrintError {
        override fun message(): String = "error"
    }
}