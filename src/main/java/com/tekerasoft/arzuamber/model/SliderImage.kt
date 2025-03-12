package com.tekerasoft.arzuamber.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "sliders")
data class SliderImage(
    val url: String,
    val lang: String,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "slider_image_id", columnDefinition = "uuid")
    val id: UUID? = null,
)
