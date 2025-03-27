package com.tekerasoft.arzuamber.controller;

import com.iyzipay.model.ThreedsInitialize;
import com.iyzipay.model.ThreedsPayment;
import com.tekerasoft.arzuamber.dto.OrderDto;
import com.tekerasoft.arzuamber.dto.request.CreatePayAtDoorRequest;
import com.tekerasoft.arzuamber.dto.request.CreatePaymentRequest;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.service.OrderService;
import com.tekerasoft.arzuamber.service.PaymentService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/order")
public class OrderController {
    private final PaymentService paymentService;
    private final OrderService orderService;

    @Value("${spring.origin.url}")
    private String originUrl;

    public OrderController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @PostMapping("/pay")
    public ResponseEntity<ThreedsInitialize> pay(@RequestBody CreatePaymentRequest req) {
        return ResponseEntity.ok(paymentService.payment(req));
    }

    @PostMapping("/complete-threeds")
    public ResponseEntity<String> completeThreeds(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Ödeme servis sağlayıcısının gönderdiği parametreleri al
            Map<String, String[]> parameters = request.getParameterMap();
            String paymentId = parameters.containsKey("paymentId") ? parameters.get("paymentId")[0] : null;
            String conversationId = parameters.containsKey("conversationId") ? parameters.get("conversationId")[0] : null;

            if (paymentId == null || conversationId == null) {
                return ResponseEntity.badRequest().body("Eksik ödeme bilgisi");
            }

            ThreedsPayment payment = paymentService.completePayment(paymentId, conversationId);

            // Ödeme başarılıysa frontend’e yönlendir
            if ("success".equalsIgnoreCase(payment.getStatus())) {
                response.sendRedirect(originUrl+"/tr/payment-success");
            } else {
                response.sendRedirect(originUrl+"/tr/payment-failure");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @GetMapping("/get-user-orders")
    public ResponseEntity<List<OrderDto>> getUserOrders(@RequestParam String email) {
        return ResponseEntity.ok(orderService.getOrderByMail(email));
    }

    @PostMapping("/create-pay-at-door-order")
    public ResponseEntity<ApiResponse<?>> createPayAtDoorOrder(@RequestBody CreatePayAtDoorRequest req) throws MessagingException {
        return ResponseEntity.ok(paymentService.payAtDoor(req));
    }

}
