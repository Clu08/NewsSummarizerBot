package actionProperties

import org.junit.jupiter.api.Test
import prod.prog.actionProperties.Action
import prod.prog.actionProperties.print.*
import kotlin.test.assertIs

class PrintActionTest {
    @Test
    fun `verify log levels order`() {
        assertIs<Action>(fatal)
        assertIs<PrintFatal>(error)
        assertIs<PrintError>(warning)
        assertIs<PrintWarning>(info)
        assertIs<PrintInfo>(debug)
    }

    companion object {
        val fatal = object : PrintFatal {
            override fun message() = "fatal"
        }
        val error = object : PrintError {
            override fun message() = "error"
        }
        val warning = object : PrintWarning {
            override fun message() = "warning"
        }
        val info = object : PrintInfo {
            override fun message() = "info"
        }
        val debug = object : PrintDebug {
            override fun message() = "debug"
        }
    }
}