package com.tekerasoft.arzuamber.model

import jakarta.persistence.*
import java.util.*

@Entity
data class Address @JvmOverloads constructor(
    val contactName: String,
    val city: String,
    val state: String,
    val country: String,
    val address: String,
    val street: String,
    val zipCode: String,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id", columnDefinition = "uuid")
    val id: UUID? = null,
)
