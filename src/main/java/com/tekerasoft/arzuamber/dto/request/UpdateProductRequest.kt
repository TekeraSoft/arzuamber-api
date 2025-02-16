package com.tekerasoft.arzuamber.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.arzuamber.dto.ColorSizeDto
import java.math.BigDecimal

data class UpdateProductRequest(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("populate")
    val populate: Boolean,
    @JsonProperty("category")
    val category: String,
    @JsonProperty("subCategory")
    val subCategory: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("price")
    val price: BigDecimal,
    @JsonProperty("colorSize")
    val colorSize: List<ColorSizeDto>,
)
