package edu.ucb.pablostify

import edu.ucb.pablostify.login.domain.valueobject.Password
import edu.ucb.pablostify.login.domain.valueobject.Username
import edu.ucb.pablostify.register.domain.valueobject.Email
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DomainValueObjectsTest {

    @Test
    fun testValidPasswordCreation() {
        val password = Password("123456")
        assertEquals("123456", password.value)
    }

    @Test
    fun testShortPasswordThrowsException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Password("12345")
        }
        assertEquals("La contraseña debe tener al menos 6 caracteres", exception.message)
    }

    @Test
    fun testValidEmailCreation() {
        val email = Email("estudiante@ucb.edu.bo")
        assertEquals("estudiante@ucb.edu.bo", email.value)
    }

    @Test
    fun testInvalidEmailThrowsException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Email("estudiante_sin_arriba")
        }
        assertEquals("Correo electrónico inválido", exception.message)
    }

    @Test
    fun testBlankUsernameThrowsException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Username("   ")
        }
        assertEquals("El nombre de usuario no puede estar vacío", exception.message)
    }
}
