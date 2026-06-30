package com.yaeldev.cursitodefundamentosandroid.data.remote.mappers

import com.yaeldev.cursitodefundamentosandroid.data.remote.dto.user.UserDTO
import com.yaeldev.cursitodefundamentosandroid.domain.models.Contacto

fun fromUserDTOListToContactoDomain(originalList: List<UserDTO>): List<Contacto> {
    val newList = originalList.map { userDTO ->
        Contacto(
            id = 0,
            first = userDTO.name.first,
            last = userDTO.name.last,
            phone = userDTO.phone,
            email = userDTO.email,
            company = "",
            favorite = false
        )
    }
    return newList
}