package com.tekerasoft.arzuamber.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SizeStockDto(
    @JsonProperty("size")
    val size: String,
    @JsonProperty("stock")
    val stock: Int,
)
