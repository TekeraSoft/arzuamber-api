package com.tekerasoft.arzuamber.dto.request

data class ReplyRequest(
    val commentId: String,
    val message: String,
    val userName: String? = "Arzuamber Moda",
)
