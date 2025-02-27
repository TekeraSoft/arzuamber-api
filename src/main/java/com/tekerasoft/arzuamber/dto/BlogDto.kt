package com.tekerasoft.arzuamber.dto

import com.tekerasoft.arzuamber.model.Blog
import com.tekerasoft.arzuamber.utils.SlugGenerator

data class BlogDto(
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
