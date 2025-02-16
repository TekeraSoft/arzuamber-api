package com.tekerasoft.arzuamber.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "category")
data class Category(
    val name: String,

    @ElementCollection
    val subCategories: List<String>,

    val lang: String,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id", columnDefinition = "uuid")
    val id: UUID? = null,
)
