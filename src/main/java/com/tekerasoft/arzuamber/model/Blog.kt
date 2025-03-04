package com.tekerasoft.arzuamber.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "blogs")
data class Blog @JvmOverloads constructor(

    val title: String,
    val slug: String,
    val category: String,
    val image: String,
    @Column(columnDefinition = "TEXT")
    val content: String,
    val lang: String,
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "uuid")
    val id: UUID? = null
)
