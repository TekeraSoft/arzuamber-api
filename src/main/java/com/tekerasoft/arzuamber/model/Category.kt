package com.tekerasoft.arzuamber.model

import jakarta.persistence.*
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Entity
@Table(name = "category")
data class Category(
    @Id
    @Column(name = "category_id")
    val id: UUID,
    val name: String,

    @ElementCollection
    var subCategories: List<String>? = listOf(),

    val lang: String,
    val image: String? = null,
)
