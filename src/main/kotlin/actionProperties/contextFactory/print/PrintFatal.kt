package prod.prog.actionProperties.contextFactory.print

import prod.prog.actionProperties.contextFactory.PropertyAdderFactory

fun interface PrintFatal : PropertyAdderFactory {
    override fun key() = PrintFatal()

    companion object : PrintFatal {
        operator fun invoke() = "PrintFatal"
        override fun value() = "no message"
    }
}

//old:

//interface PrintFatal : Action {
//    fun message(): String
//
//    companion object {
//        operator fun invoke() = object : PrintFatal {
//            override fun message(): String = "fatal"
//        }
//    }
//}

//interface PrintDebug : PrintInfo {
//    companion object {
//        operator fun invoke() = object : PrintDebug {
//            override fun message(): String = "debug"
//        }
//    }
//}