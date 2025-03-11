package com.tekerasoft.arzuamber.dto

data class BasketItemDto(
    val name: String,
    val category1: String,
    val category2: String,
    var price: String,
    val quantity: Int,
    val size: String,
    val color: String,
    val stockSizeId: String,
    val stockCode: String,
    val image: String,
)
