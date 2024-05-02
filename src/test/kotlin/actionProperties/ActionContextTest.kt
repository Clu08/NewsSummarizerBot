package prod.prog.actionProperties

import org.junit.jupiter.api.Test

class ContextTest {
    @Test
    fun `verify Context creates independent contexts each time`() {
        val emptyContext = Context()
        val anotherEmptyContext = Context().set("key", "value")
        assert(!emptyContext.has("key"))
    }
}