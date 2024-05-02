package prod.prog.actionProperties

import org.junit.jupiter.api.Test
import prod.prog.actionProperties.contextFactory.*
import prod.prog.actionProperties.contextFactory.print.*
import prod.prog.actionProperties.contextFactory.print.PrintInfo
import kotlin.test.assertIs

class PrintActionTest {
    @Test
    fun `verify log levels order`() {
        assertIs<PropertyAdderFactory>(fatal)
        assertIs<PrintFatal>(error)
        assertIs<PrintError>(warning)
        assertIs<PrintWarning>(info)
        assertIs<PrintInfo>(debug)
    }

    companion object {
        val fatal = PrintFatal { "fatal" }
        val error = PrintError { "error" }
        val warning = PrintWarning { "warning" }
        val info = PrintInfo { "info" }
        val debug = PrintDebug { "debug" }
    }
}