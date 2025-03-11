package com.tekerasoft.arzuamber.dto

import com.tekerasoft.arzuamber.model.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class OrderDto(
    val buyer: BuyerDto,
    val shippingAddress: AddressDto,
    val billingAddress: AddressDto,
    val basketItems: List<BasketItemDto>,
    val totalPrice: BigDecimal,
    val status: OrderStatus = OrderStatus.PENDING,
    val createdAt: LocalDateTime,
    val paymentId: String? = null,
    val id: UUID?
) {
    companion object {
        @JvmStatic
        fun toDto(from: Order): OrderDto {
            return OrderDto(
                from.buyer.let { b ->  BuyerDto(b.name,b.surname,b.gsmNumber,b.email,b.ip,
                    b.identityNumber,b.lastLoginDate,b.registrationDate,b.registrationAddress)
                },
                from.shippingAddress.let { a -> AddressDto(a.contactName,a.city,a.state,a.country,a.address,a.street,a.zipCode) },
                from.billingAddress.let { ba -> AddressDto(ba.contactName, ba.city, ba.state, ba.country, ba.address, ba.street, ba.zipCode) },
                from.basketItems.map { bi -> BasketItemDto(bi.name,bi.category1,bi.category2,bi.price,
                    bi.quantity,bi.size,bi.color,bi.stockSizeId, bi.stockCode,bi.image) },
                from.totalPrice,
                from.status,
                from.createdAt,
                from.paymentId,
                from.id
            )
        }

        @JvmStatic
        fun toEntity(from: OrderDto): Order {
            return Order(
                from.buyer.let { b -> Buyer(b.name,b.surname,b.gsmNumber,b.email,b.ip,
                    b.identityNumber,b.lastLoginDate,b.registrationDate,b.registrationAddress) },
                from.shippingAddress.let { a -> Address(a.contactName,a.city,a.state,a.country,a.address,a.street,a.zipCode) },
                from.billingAddress.let { ba -> Address(ba.contactName, ba.city, ba.state, ba.country, ba.address, ba.street, ba.zipCode) },
                from.basketItems.map { bi -> BasketItem(bi.name,bi.category1,bi.category2,bi.price,
                    bi.quantity,bi.size,bi.color,bi.stockSizeId, bi.stockCode, bi.image) },
                from.totalPrice,
                from.paymentId,
                from.status,
            )
        }
    }
}
