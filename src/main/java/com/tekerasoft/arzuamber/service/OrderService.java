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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PagedResourcesAssembler<OrderDto> pagedResourcesAssembler;
    private final SimpMessagingTemplate messagingTemplate;

    public OrderService(OrderRepository orderRepository, PagedResourcesAssembler<OrderDto> pagedResourcesAssembler1,
                        SimpMessagingTemplate messagingTemplate) {
        this.orderRepository = orderRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler1;
        this.messagingTemplate = messagingTemplate;
    }

    public Order save(OrderDto order) {
        try {
          return orderRepository.save(OrderDto.toEntity(order));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public PagedModel<EntityModel<OrderDto>> getAllOrders(int page, int size) {
        return pagedResourcesAssembler.toModel(
                orderRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size))
                        .map(OrderDto::toDto));
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

    public List<OrderDto> getOrderByMail(String mail) {
        return orderRepository.findUserOrdersByEmail(mail)
                .stream().map(OrderDto::toDto)
                .toList();
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
