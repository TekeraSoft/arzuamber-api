package com.tekerasoft.arzuamber.dto.request

import com.fasterxml.jackson.annotation.JsonProperty


data class CreateColorSizeRequest(
    @JsonProperty("color")
    val color: String,
    @JsonProperty("sizeStock")
    val stockSize: List<StockSizeRequest>
)
