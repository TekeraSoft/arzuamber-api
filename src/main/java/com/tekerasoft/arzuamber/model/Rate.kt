package com.tekerasoft.arzuamber.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Rate(
    val userName: String,
    val userId: String,
    val rate: Double,

    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rate_id", columnDefinition = "uuid")
    val id: UUID? = null,

    val isActive: Boolean? = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName ="product_id")
    var product: Product? = null,

    @OneToOne
    @JoinColumn(name = "comment_id", referencedColumnName = "comment_id")
    var comment: Comment? = null
)
