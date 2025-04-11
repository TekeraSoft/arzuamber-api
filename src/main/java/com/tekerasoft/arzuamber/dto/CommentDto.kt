package com.tekerasoft.arzuamber.dto

import com.tekerasoft.arzuamber.model.Comment
import java.util.UUID

data class CommentDto(
    val id: UUID?,
    val content: MutableList<ContentDto>?,
    val rate: RateDto?,
    val images: List<String>?,
    val isActive: Boolean?,
) {
    companion object {
        @JvmStatic
        fun toDto(from: Comment): CommentDto {
            return CommentDto(
                from.id,
                from.content?.map { c -> ContentDto(c.userName,c.message,c.createdAt) }?.toMutableList(),
                from.rate.let { r -> r?.let { RateDto(r.id, it.userName, r.userId, r.rate) } },
                from.images,
                from.isActive
            )
        }
    }
}
