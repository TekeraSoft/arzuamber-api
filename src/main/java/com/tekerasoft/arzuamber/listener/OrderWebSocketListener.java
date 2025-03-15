package com.tekerasoft.arzuamber.listener;

import com.tekerasoft.arzuamber.dto.OrderDto;
import com.tekerasoft.arzuamber.dto.request.OrderRequestDto;
import com.tekerasoft.arzuamber.repository.OrderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class OrderWebSocketListener {
    private final OrderRepository orderRepository;
    private final PagedResourcesAssembler<OrderDto> pagedResourcesAssembler;

    public OrderWebSocketListener(OrderRepository orderRepository, PagedResourcesAssembler<OrderDto> pagedResourcesAssembler) {
        this.orderRepository = orderRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @MessageMapping("/orders")
    @SendTo("/topic/orders")
    public void handleWebSocketSubscribeListener(OrderRequestDto requestDto) {
        System.out.println("📢 WebSocket Order Request Alındı!"); // Bu logu görmelisiniz
        System.out.println("📌 Sayfa: " + requestDto.getPage() + ", Boyut: " + requestDto.getSize());
        PagedModel<EntityModel<OrderDto>> orders =pagedResourcesAssembler.toModel(
                orderRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(requestDto.getPage(), requestDto.getSize()))
                        .map(OrderDto::toDto));

        System.out.println("📦 Gönderilen Siparişler: " + orders.getContent().size()); // Kaç sipariş gittiğini gör
        System.out.println("📊 Sipariş JSON: " + orders); // JSON olarak yazdır

    }
}
