package com.tekerasoft.arzuamber.dto

import com.tekerasoft.arzuamber.model.Role
import com.tekerasoft.arzuamber.model.User
import java.time.LocalDateTime
import java.util.UUID

data class UserAdminDto(
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: Set<Role>?,
    val lastLogin: LocalDateTime?,
    val createdAt: LocalDateTime?,
    val phoneNumber: String?,
    val id: UUID?
) {
    companion object {
        @JvmStatic
        fun toDto(user: User): UserAdminDto {
            return UserAdminDto(
                user.firstName,
                user.lastName,
                user.email,
                user.roles,
                user.lastLogin,
                user.createdAt,
                user.phoneNumber,
                user.id
            )
        }
    }
}
