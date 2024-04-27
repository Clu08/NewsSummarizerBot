package actionProperties

import org.junit.jupiter.api.Test
import prod.prog.actionProperties.ActionContext

class ActionContextTest {
    @Test
    fun `verify system creates different contexts each time`() {
        val systemContext = ActionContext.system()
        val anotherSystemContext = ActionContext.system()
        anotherSystemContext.data["key"] = "value"
        assert(!systemContext.data.containsKey("key"))
    }
}