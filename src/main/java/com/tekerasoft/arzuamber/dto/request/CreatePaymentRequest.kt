package com.tekerasoft.arzuamber.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class CreatePaymentRequest(
    @JsonProperty("paymentCard")
    val paymentCard: PaymentCard,
    @JsonProperty("buyer")
    val buyer: Buyer,
    @JsonProperty("shippingAddress")
    val shippingAddress: Address,
    @JsonProperty("basketItems")
    val basketItems: List<BasketItem>,
    @JsonProperty("billingAddress")
    val billingAddress: Address,
    @JsonProperty("shippingPrice")
    val shippingPrice: BigDecimal,
)

data class PaymentCard(
    @JsonProperty("cardHolderName")
    val cardHolderName: String,
    @JsonProperty("cardNumber")
    val cardNumber: String,
    @JsonProperty("expireMonth")
    val expireMonth: String,
    @JsonProperty("expireYear")
    val expireYear: String,
    @JsonProperty("cvc")
    val cvc: String,
)

data class Buyer(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("surname")
    val surname: String,
    @JsonProperty("gsmNumber")
    val gsmNumber: String,
    @JsonProperty("email")
    val email: String,
    @JsonProperty("ip")
    val ip: String,
    @JsonProperty("identityNumber")
    val identityNumber: String,
    @JsonProperty("lastLoginDate")
    val lastLoginDate: String,
    @JsonProperty("registrationDate")
    val registrationDate: String,
    @JsonProperty("registrationAddress")
    val registrationAddress: String,
)

data class Address(
    @JsonProperty("contactName")
    val contactName: String,
    @JsonProperty("city")
    val city: String,
    @JsonProperty("state")
    val state: String,
    @JsonProperty("country")
    val country: String,
    @JsonProperty("address")
    val address: String,
    @JsonProperty("street")
    val street: String,
    @JsonProperty("zipCode")
    val zipCode: String,
)

data class BasketItem(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("category1")
    val category1: String,
    @JsonProperty("category2")
    val category2: String,
    @JsonProperty("price")
    var price: String,
    @JsonProperty("quantity")
    val quantity: Int,
    @JsonProperty("size")
    val size: String,
    @JsonProperty("color")
    val color: String,
    @JsonProperty("stockSizeId")
    val stockSizeId: String,
    @JsonProperty("stockCode")
    val stockCode: String,
    @JsonProperty("image")
    val image: String,
)
