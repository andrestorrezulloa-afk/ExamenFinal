package edu.ucb.pablostify.login.domain.valueobject

@JvmInline
value class Password(val value: String) {
    init {
        require(value.length >= 6) { "La contraseña debe tener al menos 6 caracteres" }
    }
}
