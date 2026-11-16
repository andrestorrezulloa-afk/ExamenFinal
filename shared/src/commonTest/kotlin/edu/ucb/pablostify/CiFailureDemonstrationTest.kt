package edu.ucb.pablostify

import kotlin.test.Test
import kotlin.test.assertEquals

class CiFailureDemonstrationTest {

    @Test
    fun verificarSuma() {
        val resultadoReal = 2 + 2

        val resultadoEsperado = 5

        assertEquals(resultadoEsperado, resultadoReal)
    }
}
