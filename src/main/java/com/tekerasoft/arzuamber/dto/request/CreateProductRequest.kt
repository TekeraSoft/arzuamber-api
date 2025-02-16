package com.tekerasoft.arzuamber.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class CreateProductRequest(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("populate")
    val populate: Boolean,
    @JsonProperty("newSeason")
    val newSeason: Boolean,
    @JsonProperty("category")
    val category: String,
    @JsonProperty("subCategory")
    val subCategory: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("price")
    val price: BigDecimal,
    @JsonProperty("purchasePrice")
    val purchasePrice: BigDecimal,
    @JsonProperty("length")
    val length: String,
    @JsonProperty("colorSize")
    val colorSize: List<CreateColorSizeRequest>,
)
