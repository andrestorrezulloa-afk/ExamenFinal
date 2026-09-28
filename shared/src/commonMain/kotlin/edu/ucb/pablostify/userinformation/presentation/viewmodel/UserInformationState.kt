package edu.ucb.pablostify.userinformation.presentation.viewmodel

import edu.ucb.pablostify.userinformation.domain.model.UserInfoModel

data class UserInformationState(
    val alias: String = "",
    val user: UserInfoModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
