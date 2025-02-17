package com.tekerasoft.arzuamber.controller;

import com.iyzipay.model.ThreedsInitialize;
import com.iyzipay.model.ThreedsPayment;
import com.tekerasoft.arzuamber.dto.request.CompleteThreedsRequest;
import com.tekerasoft.arzuamber.dto.request.CreatePaymentRequest;
import com.tekerasoft.arzuamber.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/order")
public class OrderController {
    private final PaymentService paymentService;

    public OrderController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay")
    public ResponseEntity<ThreedsInitialize> pay(@RequestBody CreatePaymentRequest req) {
        return ResponseEntity.ok(paymentService.payment(req));
    }

    @PostMapping("/complete-threeds")
    public ResponseEntity<ThreedsPayment> completeThreeds(@RequestBody CompleteThreedsRequest req) {
        try {
            return ResponseEntity.ok(paymentService.completePayment(req.getPaymentId(), req.getConversationId()));
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
