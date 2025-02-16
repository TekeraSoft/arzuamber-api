package com.tekerasoft.arzuamber.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id", columnDefinition = "uuid")
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String country;
    private String city;
    private String state;
    private String address;
    private String productId;
    private BigDecimal totalPrice;
    private String color;
    private String size;
    private Integer quantity;
}
