package com.tekerasoft.arzuamber.dto

data class BuyerDto(
    val name: String,
    val surname: String,
    val gsmNumber: String,
    val email: String,
    val ip: String,
    val identityNumber: String,
    val lastLoginDate: String,
    val registrationDate: String,
    val registrationAddress: String,
)
