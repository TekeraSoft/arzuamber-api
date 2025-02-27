package com.tekerasoft.arzuamber.service;

import com.tekerasoft.arzuamber.dto.OrderDto;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.model.Order;
import com.tekerasoft.arzuamber.model.OrderStatus;
import com.tekerasoft.arzuamber.repository.OrderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PagedResourcesAssembler<OrderDto> pagedResourcesAssembler;

    public OrderService(OrderRepository orderRepository, PagedResourcesAssembler<OrderDto> pagedResourcesAssembler) {
        this.orderRepository = orderRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    public void save(OrderDto order) {
        try {
            orderRepository.save(OrderDto.toEntity(order));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public OrderDto changeOrderStatusAndReturnOrder(String paymentId) {
        Optional<Order> order = orderRepository.findByPaymentId(paymentId);
        if (order.isPresent()) {
            orderRepository.updateOrderStatusByPaymentId(paymentId, OrderStatus.PAID);
        }
        return OrderDto.toDto(order.get());
    }

    public ApiResponse<?> changeOrderStatusWithAdmin(String orderId, OrderStatus orderStatus) {
        try {
            orderRepository.updateOrderStatus(UUID.fromString(orderId),orderStatus);
            return new ApiResponse<>("Order Status Changed",null,true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public PagedModel<EntityModel<OrderDto>> getAllOrders(int page, int size) {
        return pagedResourcesAssembler.toModel(orderRepository.findAll(PageRequest.of(page, size)).map(OrderDto::toDto));
    }

    public ApiResponse<?> deleteOrder(String orderId) {
        try {
            orderRepository.deleteById(UUID.fromString(orderId));
            return new ApiResponse<>("Order Deleted",null,true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
