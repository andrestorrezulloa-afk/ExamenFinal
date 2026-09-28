package edu.ucb.pablostify.login.domain.valueobject

@JvmInline
value class Username(val value: String) {
    init {
        require(value.isNotBlank()) { "El nombre de usuario no puede estar vacío" }
    }
}
