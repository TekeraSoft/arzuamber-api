package com.tekerasoft.arzuamber.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "orders")
data class Order @JvmOverloads constructor(

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "buyer_id")
    val buyer: Buyer,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "shipping_address_id")
    val shippingAddress: Address,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "billing_address_id")
    val billingAddress: Address,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    val basketItems: List<BasketItem>,

    val totalPrice: BigDecimal,

    val paymentId: String? = null,

    @Enumerated(EnumType.STRING)
    val status: OrderStatus,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id", columnDefinition = "uuid")
    val id: UUID? = null,
)
