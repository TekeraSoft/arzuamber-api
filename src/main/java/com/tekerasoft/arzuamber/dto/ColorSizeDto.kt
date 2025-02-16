package com.tekerasoft.arzuamber.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ColorSizeDto(
    @JsonProperty("color")
    val color: String,
    @JsonProperty("stockSize")
    val stockSize: Set<SizeStockDto>,
    @JsonProperty("stockCode")
    val stockCode: String,
    @JsonProperty("images")
    val images: List<String>?
)
