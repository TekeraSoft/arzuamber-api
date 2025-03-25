package com.tekerasoft.arzuamber.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class CreatePayAtDoorRequest(
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