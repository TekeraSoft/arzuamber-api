package com.tekerasoft.arzuamber.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "contacts")
data class Contact(

    val name: String,
    val surname: String,
    val email: String,
    val message: String,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "contact_id", columnDefinition = "uuid")
    val id: UUID? = null,
)
