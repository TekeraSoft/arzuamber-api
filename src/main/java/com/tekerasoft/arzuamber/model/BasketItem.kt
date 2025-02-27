package com.tekerasoft.arzuamber.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.*

@Entity
data class BasketItem @JvmOverloads constructor(
    val name: String,
    val category1: String,
    val category2: String,
    var price: String,
    val quantity: Int,
    val size: String,
    val color: String,
    val stockSizeId: String,
    val stockCode: String,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "basket_id", columnDefinition = "uuid")
    val id: UUID? = null,

)
