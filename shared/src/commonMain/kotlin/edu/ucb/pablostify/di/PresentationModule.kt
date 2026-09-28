package edu.ucb.pablostify.di

import edu.ucb.pablostify.login.presentation.viewmodel.LoginViewModel
import edu.ucb.pablostify.moviedetail.presentation.viewmodel.MovieDetailViewModel
import edu.ucb.pablostify.movielist.presentation.viewmodel.MovieListViewModel
import edu.ucb.pablostify.profile.presentation.viewmodel.ProfileViewModel
import edu.ucb.pablostify.register.presentation.viewmodel.RegisterViewModel
import edu.ucb.pablostify.userinformation.presentation.viewmodel.UserInformationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::MovieListViewModel)
    viewModelOf(::MovieDetailViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::UserInformationViewModel)
}
