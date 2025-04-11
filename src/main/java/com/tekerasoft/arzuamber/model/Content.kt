package com.tekerasoft.arzuamber.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Content(
    val userName: String,
    val message: String,
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", referencedColumnName ="comment_id")
    val comment: Comment? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "content_id", columnDefinition = "uuid")
    val id: UUID? = null
)
