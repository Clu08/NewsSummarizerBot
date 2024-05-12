package prod.prog.actionProperties

import common.UnitTest
import io.kotest.core.spec.style.StringSpec

class ContextTest : StringSpec({
    tags(UnitTest)

    "verify Context creates independent contexts each time" {
        val emptyContext = Context()
        Context().set("key", "value")
        assert(!emptyContext.has("key"))
    }
})