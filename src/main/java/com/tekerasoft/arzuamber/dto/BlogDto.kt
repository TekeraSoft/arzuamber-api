package com.tekerasoft.arzuamber.dto

import com.tekerasoft.arzuamber.model.Blog
import com.tekerasoft.arzuamber.utils.SlugGenerator
import java.util.UUID

data class BlogDto(
    val id: UUID?,
    val title: String,
    val slug: String,
    val category: String,
    val image: String,
    val content: String
) {
    companion object {
        @JvmStatic
        fun toDto(from: Blog): BlogDto {
            return BlogDto(
                from.id,
                from.title,
                from.slug,
                from.category,
                from.image,
                from.content
            )
        }

        @JvmStatic
        fun toEntity(from: BlogDto, lang: String): Blog {
            return Blog(from.title, SlugGenerator.generateSlug(from.title),from.category, from.image, from.content, lang)
        }
    }
}
