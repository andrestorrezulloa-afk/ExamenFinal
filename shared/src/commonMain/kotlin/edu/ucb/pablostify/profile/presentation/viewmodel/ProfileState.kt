package edu.ucb.pablostify.profile.presentation.viewmodel

import edu.ucb.pablostify.profile.domain.model.UserProfile

data class ProfileState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
