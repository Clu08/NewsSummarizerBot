package prod.prog.actionProperties.print

import prod.prog.actionProperties.Action
import prod.prog.actionProperties.ActionClass

interface PrintFatal : Action {
    fun message(): String

    companion object : ActionClass(), PrintFatal {
        override fun message(): String = "fatal"
    }
}