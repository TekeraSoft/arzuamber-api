package com.tekerasoft.arzuamber.dto

import com.tekerasoft.arzuamber.model.Product
import java.math.BigDecimal
import java.util.*

data class ProductDto @JvmOverloads constructor(
    val id: UUID?,
    val name: String,
    val slug: String,
    val populate: Boolean,
    val newSeason: Boolean,
    val category: String,
    val subCategory: String,
    val description: String,
    val price: BigDecimal,
    val lang: String,
    val length: String,
    val isActive: Boolean?,
    val colorSize: List<ColorSizeDto>?,
    val purchasePrice: BigDecimal?,
    val discountPrice: BigDecimal? = BigDecimal.ZERO,
    val totalStock: Int? = 0,
    val comments: MutableList<CommentDto>?,
    val rates: MutableList<RateDto>?
) {
    companion object {
        @JvmStatic
        fun toDto(from: Product): ProductDto {
            return ProductDto(
                from.id,
                from.name,
                from.slug,
                from.populate,
                from.newSeason,
                from.category,
                from.subCategory,
                from.description,
                from.price,
                from.lang,
                from.length,
                from.isActive,
                from.colorSize.map { cs -> ColorSizeDto(cs.color,cs.stockSize.map { ss -> SizeStockDto(ss.id,ss.size,ss.stock) }.toSet(),cs.stockCode,cs.images) },
                from.purchasePrice,
                from.discountPrice,
                from.totalStock,
                from.comments?.filter { it.isActive == true }
                ?.sortedByDescending { it.createdAt }
                ?.map { c -> CommentDto(c.id,c.content?.map { con -> ContentDto(con.userName,con.message,con.createdAt) }?.toMutableList(),
                    c.rate?.let { r -> RateDto(r.id,r.userName,r.userId,r.rate) },c.images,c.isActive) }
                ?.toMutableList(),
                from.rates?.map { r -> RateDto(r.id,r.userName,r.userId,r.rate) }?.toMutableList(),
            )
        }

        @JvmStatic
        fun toAdminDto(from: Product): ProductDto {
            return ProductDto(
                from.id,
                from.name,
                from.slug,
                from.populate,
                from.newSeason,
                from.category,
                from.subCategory,
                from.description,
                from.price,
                from.lang,
                from.length,
                from.isActive,
                from.colorSize.map { cs -> ColorSizeDto(cs.color,cs.stockSize.map { ss -> SizeStockDto(ss.id,ss.size,ss.stock) }.toSet(),cs.stockCode,cs.images) },
                from.purchasePrice,
                from.discountPrice,
                from.totalStock,
                from.comments
                    ?.sortedByDescending { it.createdAt }
                    ?.map { c -> CommentDto(c.id,c.content?.map { con -> ContentDto(con.userName,con.message,con.createdAt) }?.toMutableList(),c.rate?.let { r -> RateDto(r.id,r.userName,r.userId,r.rate) },c.images,c.isActive) }
                    ?.toMutableList(),
                from.rates?.sortedByDescending { it.createdAt }?.map { r -> RateDto(r.id,r.userName,r.userId,r.rate) }?.toMutableList(),
            )
        }
    }
}
