package prod.prog.actionProperties

import common.UnitTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.types.beInstanceOf
import prod.prog.actionProperties.contextFactory.PropertyAdderFactory
import prod.prog.actionProperties.contextFactory.print.*

class PrintActionTest : StringSpec({
    tags(UnitTest)

    val fatal = PrintFatal { "fatal" }
    val error = PrintError { "error" }
    val warning = PrintWarning { "warning" }
    val info = PrintInfo { "info" }
    val debug = PrintDebug { "debug" }

    "verify log levels order" {
        fatal should beInstanceOf<PropertyAdderFactory>()
        error should beInstanceOf<PrintFatal>()
        warning should beInstanceOf<PrintError>()
        info should beInstanceOf<PrintWarning>()
        debug should beInstanceOf<PrintInfo>()
    }
})