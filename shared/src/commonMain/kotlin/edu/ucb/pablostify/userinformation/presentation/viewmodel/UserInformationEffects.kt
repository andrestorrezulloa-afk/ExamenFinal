package edu.ucb.pablostify.userinformation.presentation.viewmodel

sealed interface UserInformationEffects {
    data object NavigateBack : UserInformationEffects
    data class ShowMessage(val message: String) : UserInformationEffects
}
