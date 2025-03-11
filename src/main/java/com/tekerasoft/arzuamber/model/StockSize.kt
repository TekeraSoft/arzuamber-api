package com.tekerasoft.arzuamber.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "stock_size")
data class StockSize @JvmOverloads constructor(

    val size: String,
    val stock: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_size_id", referencedColumnName = "color_size_id")
    var colorSize: ColorSize? = null,  // ColorSize ile ilişkiyi burada belirtiyoruz.

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="stock_size_id", columnDefinition = "uuid")
    val id: UUID? = null,
)
