package edu.ucb.pablostify.core.domain.model

data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val avatarUrl: String? = null
)
