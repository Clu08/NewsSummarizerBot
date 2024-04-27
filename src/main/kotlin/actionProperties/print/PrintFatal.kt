package prod.prog.actionProperties.print

import prod.prog.actionProperties.Action

interface PrintFatal : Action {
    fun message(): String

    companion object {
        operator fun invoke() = object : PrintFatal {
            override fun message(): String = "fatal"
        }
    }
}