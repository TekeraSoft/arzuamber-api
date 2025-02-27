package com.tekerasoft.arzuamber.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class SizeStockDto(
    @JsonProperty("id")
    val id: UUID?,
    @JsonProperty("size")
    val size: String,
    @JsonProperty("stock")
    val stock: Int,
)
