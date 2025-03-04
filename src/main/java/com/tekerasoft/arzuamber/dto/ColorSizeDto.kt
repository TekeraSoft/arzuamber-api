package com.tekerasoft.arzuamber.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ColorSizeDto(
    @JsonProperty("color")
    val color: String,
    @JsonProperty("stockSize")
    val stockSize: Set<SizeStockDto>,
    @JsonProperty("stockCode")
    val stockCode: String? = null,
    @JsonProperty("images")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val images: List<String>? = listOf()
)
