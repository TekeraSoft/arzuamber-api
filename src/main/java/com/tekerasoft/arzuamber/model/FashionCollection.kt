package com.tekerasoft.arzuamber.model

import jakarta.persistence.Column
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import java.util.UUID
import jakarta.persistence.Entity

@Entity
open class FashionCollection(
    @Id
    @GeneratedValue(generator = "uuid")
    open var id: UUID? = null,

    open var collectionName: String,

    open var slug: String? = null,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "fashion_collection_products",
        joinColumns = [JoinColumn(name = "fashion_collection_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")]
    )
    open var products: MutableSet<Product> = mutableSetOf(),

    open var image: String? = null,

    @Column(columnDefinition = "TEXT")
    open var description: String? = null,

    open var isActive: Boolean = true,
)
