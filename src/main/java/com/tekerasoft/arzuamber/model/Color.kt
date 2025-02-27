package com.tekerasoft.arzuamber.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "colors")
data class Color @JvmOverloads constructor(
    val name: String,
    val lang: String,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id", columnDefinition = "uuid")
    val id: UUID? = null,
)
