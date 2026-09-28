package edu.ucb.pablostify.di

import edu.ucb.pablostify.login.data.LoginRepositoryImpl
import edu.ucb.pablostify.login.domain.repository.LoginRepository
import edu.ucb.pablostify.moviedetail.data.MovieDetailRepositoryImpl
import edu.ucb.pablostify.moviedetail.domain.repository.MovieDetailRepository
import edu.ucb.pablostify.movielist.data.MovieRepositoryImpl
import edu.ucb.pablostify.movielist.domain.repository.MovieRepository
import edu.ucb.pablostify.profile.data.ProfileRepositoryImpl
import edu.ucb.pablostify.profile.domain.repository.ProfileRepository
import edu.ucb.pablostify.register.data.RegisterRepositoryImpl
import edu.ucb.pablostify.register.domain.repository.RegisterRepository
import edu.ucb.pablostify.userinformation.data.datasource.GithubRemoteDataSource
import edu.ucb.pablostify.userinformation.data.repository.GithubRepositoryImpl
import edu.ucb.pablostify.userinformation.data.service.GitHubApiService
import edu.ucb.pablostify.userinformation.domain.repository.GithubRepository
import edu.ucb.pablostify.openmeteo.data.datasource.OpenMeteoRemoteDataSource
import edu.ucb.pablostify.openmeteo.data.repository.OpenMeteoRepositoryImpl
import edu.ucb.pablostify.openmeteo.data.service.OpenMeteoApiService
import edu.ucb.pablostify.openmeteo.domain.repository.OpenMeteoRepository
import org.koin.dsl.module

val dataModule = module {

    single<LoginRepository> {
        LoginRepositoryImpl()
    }

    single<RegisterRepository> {
        RegisterRepositoryImpl()
    }

    single<MovieRepository> {
        MovieRepositoryImpl()
    }

    single<MovieDetailRepository> {
        MovieDetailRepositoryImpl()
    }

    single<ProfileRepository> {
        ProfileRepositoryImpl()
    }

    single<GithubRemoteDataSource> {
        GitHubApiService()
    }

    single<GithubRepository> {
        GithubRepositoryImpl(get())
    }

    single<OpenMeteoRemoteDataSource> {
        OpenMeteoApiService()
    }

    single<OpenMeteoRepository> {
        OpenMeteoRepositoryImpl(get())
    }
}