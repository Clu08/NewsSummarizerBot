package prod.prog.request.transformer

import common.UnitTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.exhaustive
import kotlinx.coroutines.future.await
import prod.prog.common.MutableSingleton
import prod.prog.request.source.ConstantSource
import prod.prog.request.source.invoke

class IdTransformerTest : StringSpec({
    tags(UnitTest)

    val values = listOf(1, "1", MutableSingleton(1)).exhaustive()

    "invoke does not change the source" {
        checkAll(values) { value ->
            ConstantSource(value)().await() shouldBe value
        }
    }
})