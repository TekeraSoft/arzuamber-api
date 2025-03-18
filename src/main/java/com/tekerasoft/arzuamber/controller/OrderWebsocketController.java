package com.tekerasoft.arzuamber.controller;

import com.tekerasoft.arzuamber.dto.OrderDto;
import com.tekerasoft.arzuamber.dto.request.OrderRequestDto;
import com.tekerasoft.arzuamber.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderWebsocketController {
    private final OrderRepository orderRepository;
    private final PagedResourcesAssembler<OrderDto> pagedResourcesAssembler;
    private final SimpMessagingTemplate messagingTemplate;
    private static final Logger log = LoggerFactory.getLogger(OrderWebsocketController.class);

    public OrderWebsocketController(OrderRepository orderRepository,
                                    PagedResourcesAssembler<OrderDto> pagedResourcesAssembler,
                                  SimpMessagingTemplate messagingTemplate) {
        this.orderRepository = orderRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.messagingTemplate = messagingTemplate;
    }

//    @MessageMapping("/orders")  // /app/orders istekleri
//    @SendTo("/topic/orders")   // /topic/orders'a gönderilecek
//    public List<OrderDto> handleOrderRequest(OrderRequestDto requestDto) {
//        log.info("📢 WebSocket Order Request ALINDI! Sayfa: {}, Boyut: {}", requestDto.getPage(), requestDto.getSize());
//        return orderRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(requestDto.getPage(), requestDto.getSize()))
//                .stream().map(OrderDto::toDto).toList();
//    }
}
