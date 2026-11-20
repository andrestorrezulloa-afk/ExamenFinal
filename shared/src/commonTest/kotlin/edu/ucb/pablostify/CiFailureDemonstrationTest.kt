package edu.ucb.pablostify

import kotlin.test.Test
import kotlin.test.assertEquals

class CiFailureDemonstrationTest {

    @Test
    fun verificarSuma() {
        val resultadoReal = 2 + 2

        // CAMBIAR ESTE NUMERO:
        // 5 = RED (falla)
        // 4 = GREEN (pasa)
        val resultadoEsperado = 4

        assertEquals(resultadoEsperado, resultadoReal)
    }
}
