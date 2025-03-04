package com.tekerasoft.arzuamber.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "color_size")
data class ColorSize @JvmOverloads constructor(

    val color: String,
    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)
    @JoinColumn(name = "color_size_id")
    var stockSize: List<StockSize>,
    val stockCode: String,

    @ElementCollection(fetch = FetchType.EAGER)
    val images: List<String>?,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "color_size_id", columnDefinition = "uuid")
    val id: UUID? = null
)
