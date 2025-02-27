package com.tekerasoft.arzuamber.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class StockSizeRequest(
    @JsonProperty("size")
    val size: String,
    @JsonProperty("stock")
    val stock: Int
)
