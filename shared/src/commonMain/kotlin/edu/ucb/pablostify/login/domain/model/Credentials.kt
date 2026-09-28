package edu.ucb.pablostify.login.domain.model

import edu.ucb.pablostify.login.domain.valueobject.Password
import edu.ucb.pablostify.login.domain.valueobject.Username

data class Credentials(
    val username: Username,
    val password: Password
)
