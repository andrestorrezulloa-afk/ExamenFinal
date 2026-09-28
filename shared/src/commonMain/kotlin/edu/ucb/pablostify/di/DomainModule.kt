package edu.ucb.pablostify.di

import edu.ucb.pablostify.login.domain.usecase.LoginUseCase
import edu.ucb.pablostify.moviedetail.domain.usecase.GetMovieDetailUseCase
import edu.ucb.pablostify.moviedetail.domain.usecase.SubmitReviewUseCase
import edu.ucb.pablostify.movielist.domain.usecase.GetMovieListUseCase
import edu.ucb.pablostify.profile.domain.usecase.GetProfileUseCase
import edu.ucb.pablostify.profile.domain.usecase.LogoutUseCase
import edu.ucb.pablostify.register.domain.usecase.RegisterUseCase
import edu.ucb.pablostify.userinformation.domain.usecase.FindAliasUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::LoginUseCase)
    singleOf(::RegisterUseCase)
    singleOf(::GetMovieListUseCase)
    singleOf(::GetMovieDetailUseCase)
    singleOf(::SubmitReviewUseCase)
    singleOf(::GetProfileUseCase)
    singleOf(::LogoutUseCase)
    singleOf(::FindAliasUseCase)
}
