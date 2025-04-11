package com.tekerasoft.arzuamber.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "product")
data class Product @JvmOverloads constructor(

    val name: String,
    val slug: String,
    val populate: Boolean,
    val newSeason: Boolean,
    val category: String,
    val subCategory: String,
    @Column(columnDefinition = "TEXT")
    val description: String,
    var price: BigDecimal,
    val lang: String,
    val length: String,

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var colorSize: List<ColorSize>,

    var totalStock: Int? = 0,

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var comments: MutableList<Comment>? = mutableListOf(),

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var rates: MutableList<Rate>? = mutableListOf(),

    val purchasePrice: BigDecimal? = BigDecimal.ZERO,
    val discountPrice: BigDecimal? = BigDecimal.ZERO,

    val updatedAt: LocalDateTime? = LocalDateTime.now(),
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id", columnDefinition = "uuid")
    val id: UUID? = null,

    val isActive: Boolean? = true,
    )

