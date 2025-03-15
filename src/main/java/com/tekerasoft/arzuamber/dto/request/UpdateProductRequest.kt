package com.tekerasoft.arzuamber.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.arzuamber.dto.ColorSizeDto
import java.math.BigDecimal

data class UpdateProductRequest(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("slug")
    val slug: String,
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
    @JsonProperty("length")
    val length: String,
    @JsonProperty("purchasePrice")
    val purchasePrice: BigDecimal,
    @JsonProperty("discountPrice")
    val discountPrice: BigDecimal,
    @JsonProperty("colorSize")
    val colorSize: List<ColorSizeDto>,
    @JsonProperty("deletedImages")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val deletedImages: List<String>? = listOf(),
)
