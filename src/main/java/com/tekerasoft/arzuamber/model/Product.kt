package com.tekerasoft.arzuamber.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.HashSet
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
    val description: String,
    var price: BigDecimal,
    val lang: String,
    val length: String,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val colorSize: Set<ColorSize>,

    val totalStock: Int? = 0,
    val purchasePrice: BigDecimal?= BigDecimal.ZERO,
    val discountPrice: BigDecimal? = BigDecimal.ZERO,

    val updatedAt: LocalDateTime? = LocalDateTime.now(),
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id", columnDefinition = "uuid")
    val id: UUID? = null,

    )
