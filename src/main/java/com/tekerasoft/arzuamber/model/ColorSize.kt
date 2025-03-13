package com.tekerasoft.arzuamber.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "color_size")
data class ColorSize @JvmOverloads constructor(

    val color: String,
    @OneToMany(mappedBy = "colorSize", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var stockSize: List<StockSize>,
    val stockCode: String,

    @ElementCollection(fetch = FetchType.EAGER)
    val images: List<String>?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    var product: Product? = null, // product ile ilişki burada ekleniyor

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "color_size_id", columnDefinition = "uuid")
    val id: UUID? = null
)
