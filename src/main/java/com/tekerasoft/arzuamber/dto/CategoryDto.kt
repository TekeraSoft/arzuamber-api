package com.tekerasoft.arzuamber.dto

import com.tekerasoft.arzuamber.dto.request.CreateCategoryRequest
import com.tekerasoft.arzuamber.model.Category
import java.util.UUID

data class CategoryDto(
    val id: UUID,
    val name: String,
    val subCategories: List<String>?,
    val lang: String,
    val image: String?,
) {
    companion object {
        @JvmStatic
        fun createCategoryEntity(from: CreateCategoryRequest,imgUrl: String): Category {
            return Category(
                from.id,
                from.name,
                from.subCategories,
                from.lang,
                imgUrl
            )
        }

        @JvmStatic
        fun toDto(from:Category): CategoryDto {
            return CategoryDto(
                from.id,
                from.name,
                from.subCategories,
                from.lang,
                from.image
            )
        }
    }
}
