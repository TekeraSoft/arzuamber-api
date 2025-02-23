package com.tekerasoft.arzuamber.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "size_stock")
data class StockSize @JvmOverloads constructor(

    val size: String,
    val stock: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="size_stock_id", columnDefinition = "uuid")
    val id: UUID? = null,
)
