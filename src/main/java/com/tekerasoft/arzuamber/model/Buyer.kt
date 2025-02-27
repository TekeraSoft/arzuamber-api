package com.tekerasoft.arzuamber.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.util.*

@Entity
data class Buyer @JvmOverloads constructor(
    val name: String,
    val surname: String,
    val gsmNumber: String,
    val email: String,
    val ip: String,
    val identityNumber: String,
    val lastLoginDate: String,
    val registrationDate: String,
    val registrationAddress: String,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "buyer_id", columnDefinition = "uuid")
    val id: UUID? = null
)
