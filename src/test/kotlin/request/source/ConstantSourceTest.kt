package request.source

import common.MutableSingleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import prod.prog.request.source.ConstantSource

class ConstantSourceTest {
    @ParameterizedTest
    @MethodSource("params")
    fun <T> `validate getSource does not change the source`(t: T) {
        assertEquals(t, ConstantSource(t).getSource())
    }

    @ParameterizedTest
    @MethodSource("params")
    fun <T> `validate invoke does not change the source`(t: T) {
        assertEquals(t, ConstantSource(t)().get())
    }

    companion object {
        @JvmStatic
        private fun params() =
            listOf(1, 1.toString(), MutableSingleton(1))
                .map { Arguments.of(it) }
    }
}
