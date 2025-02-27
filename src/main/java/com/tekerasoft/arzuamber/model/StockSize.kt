package com.tekerasoft.arzuamber.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "stock_size")
data class StockSize @JvmOverloads constructor(

    val size: String,
    val stock: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="stock_size_id", columnDefinition = "uuid")
    val id: UUID? = null,
)
