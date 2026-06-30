package com.yaeldev.cursitodefundamentosandroid.data.remote.dto.user

data class UserResponseDTO(
    val info: Info,
    val results: List<UserDTO>
)