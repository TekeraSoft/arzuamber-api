package com.tekerasoft.arzuamber.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class SizeStockRequest(
    @JsonProperty("size")
    val size: String,
    @JsonProperty("stock")
    val stock: Int
)
