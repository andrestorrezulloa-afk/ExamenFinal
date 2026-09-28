package edu.ucb.pablostify.userinformation.presentation.viewmodel

sealed interface UserInformationEvents {
    data class OnAliasChanged(val value: String) : UserInformationEvents
    data object OnSubmit : UserInformationEvents
    data object OnBack : UserInformationEvents
}
