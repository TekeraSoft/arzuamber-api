package com.tekerasoft.arzuamber.model

import com.tekerasoft.arzuamber.utils.StockCodeGenerator
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "color_size")
data class ColorSize @JvmOverloads constructor(

    val color: String,
    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)
    @JoinColumn(name = "color_size_id")
    val sizeStock: Set<SizeStock>,
    val stockCode: String,

    val totalStock: Int? = 0,

    @ElementCollection
    @CollectionTable(name = "images", joinColumns = [JoinColumn(name = "color_size_id")])
    val images: List<String>?,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "color_size_id", columnDefinition = "uuid")
    val id: UUID? = null
)
