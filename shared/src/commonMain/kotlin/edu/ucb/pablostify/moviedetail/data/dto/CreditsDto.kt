package edu.ucb.pablostify.moviedetail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreditsDto(
    val cast: List<CastDto> = emptyList()
)

@Serializable
data class CastDto(
    val id: Int,
    val name: String,

    @SerialName("profile_path")
    val profilePath: String? = null
)