package edu.ucb.pablostify.userinformation.data.mapper

import edu.ucb.pablostify.userinformation.data.dto.UserInfoDto
import edu.ucb.pablostify.userinformation.domain.model.UserInfoModel

fun UserInfoDto.toDomain(alias: String): UserInfoModel = UserInfoModel(
    email = email ?: "",
    company = "",
    avatarUrl = avatarUrl ?: "",
    alias = alias
)
