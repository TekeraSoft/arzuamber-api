package com.tekerasoft.arzuamber.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class Notification(
    @Enumerated(EnumType.STRING)
    val type: NotificationType,
    val head: String,
    val content: String,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    val isActive: Boolean? = true,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "notification_id", columnDefinition = "uuid")
    val id: UUID? = null
)
