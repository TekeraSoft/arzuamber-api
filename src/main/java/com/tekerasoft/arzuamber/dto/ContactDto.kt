package com.tekerasoft.arzuamber.dto

import com.tekerasoft.arzuamber.model.Contact
import java.util.UUID

data class ContactDto(
    val id: UUID?,
    val name: String,
    val surname: String,
    val message: String
) {
    companion object {
        @JvmStatic
        fun toDto(from: Contact): ContactDto {
            return ContactDto(
                from.id,
                from.name,
                from.surname,
                from.message
            )
        }
    }
}
