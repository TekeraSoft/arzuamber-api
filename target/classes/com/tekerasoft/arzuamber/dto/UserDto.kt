package com.tekerasoft.arzuamber.dto

import com.tekerasoft.arzuamber.dto.request.CreateUserRequest
import com.tekerasoft.arzuamber.model.User
import org.springframework.security.crypto.password.PasswordEncoder

data class UserDto(
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: String,
) {
    companion object {
        @JvmStatic
        fun createUserEntity(from: CreateUserRequest,encoder: PasswordEncoder): User {
            return User(
                from.name,
                from.surname,
                from.email,
                encoder.encode(from.password)
            )
        }
    }
}
