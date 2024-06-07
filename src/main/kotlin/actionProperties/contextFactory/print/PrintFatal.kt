package prod.prog.actionProperties.contextFactory.print

import prod.prog.actionProperties.contextFactory.PropertyAdderFactory

fun interface PrintFatal : PropertyAdderFactory {
    override fun key() = PrintFatal()

    companion object : PrintFatal {
        operator fun invoke() = "PrintFatal"
        override fun value() = "no message"
    }
}