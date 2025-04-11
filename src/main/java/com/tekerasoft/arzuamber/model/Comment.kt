package com.tekerasoft.arzuamber.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "comment")
data class Comment(
    @OneToMany(mappedBy = "comment", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val content: MutableList<Content>? = mutableListOf(),

    @ElementCollection
    @CollectionTable(
        name = "comment_images",
        joinColumns = [JoinColumn(name = "comment_id", referencedColumnName = "comment_id")]
    )
    @Column(name = "image_url")
    val images: List<String>? = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    val product: Product? = null,

    val createdAt: LocalDateTime? = LocalDateTime.now(),
    var isActive: Boolean? = false,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id", columnDefinition = "uuid")
    var id: UUID? = null,

    @OneToOne(mappedBy = "comment", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "rate_id")
    var rate: Rate? = null,
)
