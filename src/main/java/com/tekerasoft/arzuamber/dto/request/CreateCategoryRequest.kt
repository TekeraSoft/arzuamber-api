package com.tekerasoft.arzuamber.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class CreateCategoryRequest(
    @JsonProperty("id")
    val id: UUID,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("subCategories")
    val subCategories: List<String>,
    @JsonProperty("lang")
    val lang: String,
)
