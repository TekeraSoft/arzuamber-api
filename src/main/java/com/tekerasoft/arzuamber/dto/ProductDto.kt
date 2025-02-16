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
    val colorSize: Set<ColorSizeDto>,
    val discountPrice: BigDecimal? = BigDecimal.ZERO,
    val purchasePrice: BigDecimal? = BigDecimal.ZERO,
    val totalStock: Int? = 0,
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
                from.colorSize.map { cs -> ColorSizeDto(cs.color,cs.sizeStock.map { ss -> SizeStockDto(ss.size,ss.stock) }.toSet(),cs.stockCode,cs.images) }.toSet(),
                from.discountPrice
            )
        }

        @JvmStatic
        fun toDtoAdmin(from: Product): ProductDto {
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
                from.colorSize.map { cs -> ColorSizeDto(cs.color,cs.sizeStock.map { ss-> SizeStockDto(ss.size,ss.stock) }.toSet(),cs.stockCode,cs.images) }.toSet(),
                from.purchasePrice,
                from.discountPrice,
                from.totalStock
            )
        }
    }
}
