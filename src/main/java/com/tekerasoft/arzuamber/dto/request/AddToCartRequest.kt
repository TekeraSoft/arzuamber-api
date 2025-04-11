package com.tekerasoft.arzuamber.dto.request

data class AddToCartRequest(
    val productName: String,
    val count: Int,
    val stockCode: String,
    val userName: String? = "Guest"
)
