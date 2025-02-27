package com.tekerasoft.arzuamber.controller;

import com.tekerasoft.arzuamber.service.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/user")
public class UserController {
    private final OrderService orderService;

    public UserController(OrderService orderService) {
        this.orderService = orderService;
    }

}
