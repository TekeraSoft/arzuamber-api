package com.tekerasoft.arzuamber.dto.request

data class CreateUserRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
)
