package com.tekerasoft.arzuamber.dto.request

import java.util.UUID

data class CreateCategoryRequest(
    val name: String,
    val subCategories: List<String>,
    val lang: String,
    val id: UUID
)
