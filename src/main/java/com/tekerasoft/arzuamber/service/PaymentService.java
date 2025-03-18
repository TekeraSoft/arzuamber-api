package com.tekerasoft.arzuamber.service;

import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreatePaymentRequest;
import com.iyzipay.request.CreateThreedsPaymentRequestV2;
import com.tekerasoft.arzuamber.dto.AddressDto;
import com.tekerasoft.arzuamber.dto.BasketItemDto;
import com.tekerasoft.arzuamber.dto.BuyerDto;
import com.tekerasoft.arzuamber.dto.OrderDto;
import com.tekerasoft.arzuamber.model.Order;
import com.tekerasoft.arzuamber.model.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PaymentService {

    @Value("${spring.payment.callback-url}")
    private String callBackUrl;

    private final Options options;
    private final OrderService orderService;
    private final ProductService productService;
    private final SimpMessagingTemplate messagingTemplate;

    public PaymentService(Options options, OrderService orderService, ProductService productService, SimpMessagingTemplate messagingTemplate) {
        this.options = options;
        this.orderService = orderService;
        this.productService = productService;
        this.messagingTemplate = messagingTemplate;
    }

    public ThreedsInitialize payment(com.tekerasoft.arzuamber.dto.request.CreatePaymentRequest req) {
        try {
            CreatePaymentRequest request = new CreatePaymentRequest();
            request.setLocale(Locale.TR.getValue());
            request.setConversationId("arzu_amber_moda");
            request.setCurrency(Currency.TRY.name());
            request.setInstallment(1);
            request.setBasketId("B67832");
            request.setPaymentChannel(PaymentChannel.WEB.name());
            request.setPaymentGroup(PaymentGroup.PRODUCT.name());

            PaymentCard paymentCard = new PaymentCard();
            paymentCard.setCardHolderName(req.getPaymentCard().getCardHolderName());
            paymentCard.setCardNumber(req.getPaymentCard().getCardNumber());
            paymentCard.setExpireMonth(req.getPaymentCard().getExpireMonth());
            paymentCard.setExpireYear(req.getPaymentCard().getExpireYear());
            paymentCard.setCvc(req.getPaymentCard().getCvc());
            paymentCard.setRegisterCard(0);
            request.setPaymentCard(paymentCard);

            Buyer buyer = new Buyer();
            buyer.setId(req.getBuyer().getId());
            buyer.setName(req.getBuyer().getName());
            buyer.setSurname(req.getBuyer().getSurname());
            buyer.setGsmNumber(req.getBuyer().getGsmNumber());
            buyer.setEmail(req.getBuyer().getEmail());
            buyer.setIdentityNumber(req.getBuyer().getIdentityNumber());
            buyer.setLastLoginDate(req.getBuyer().getLastLoginDate());
            buyer.setRegistrationDate(req.getBuyer().getRegistrationDate());
            buyer.setRegistrationAddress(req.getBuyer().getRegistrationAddress());
            buyer.setIp(req.getBuyer().getIp());
            buyer.setCity(req.getShippingAddress().getCity());
            buyer.setCountry(req.getShippingAddress().getCountry());
            buyer.setZipCode(req.getShippingAddress().getZipCode());
            request.setBuyer(buyer);

            Address shippingAddress = new Address();
            shippingAddress.setContactName(req.getShippingAddress().getContactName());
            shippingAddress.setCity(req.getShippingAddress().getCity());
            shippingAddress.setCountry(req.getShippingAddress().getCountry());
            shippingAddress.setAddress(req.getShippingAddress().getAddress());
            shippingAddress.setZipCode(req.getShippingAddress().getZipCode());
            request.setShippingAddress(shippingAddress);

            Address billingAddress = new Address();
                billingAddress.setContactName(req.getBillingAddress().getContactName());
                billingAddress.setCity(req.getBillingAddress().getCity());
                billingAddress.setCountry(req.getBillingAddress().getCountry());
                billingAddress.setAddress(req.getBillingAddress().getAddress());
                billingAddress.setZipCode(req.getBillingAddress().getZipCode());
                request.setBillingAddress(billingAddress);

            request.setCallbackUrl(callBackUrl);

            List<BasketItem> basketItems = new ArrayList<>();
            for (com.tekerasoft.arzuamber.dto.request.BasketItem bi : req.getBasketItems()) {
                BasketItem basketI = new BasketItem();
                basketI.setId(bi.getId());
                basketI.setName(bi.getName());
                basketI.setCategory1(bi.getCategory1());
                basketI.setCategory2(bi.getCategory2());
                basketI.setItemType(BasketItemType.PHYSICAL.name());

                // Fiyatı quantity (adet) ile çarpıyoruz
                BigDecimal totalItemPrice = new BigDecimal(bi.getPrice()).multiply(new BigDecimal(bi.getQuantity()));
                basketI.setPrice(totalItemPrice);

                basketItems.add(basketI);
            }

            if (req.getShippingPrice().compareTo(BigDecimal.ZERO) > 0) {
                BasketItem shippingItem = new BasketItem();
                shippingItem.setId("SHIPPING");
                shippingItem.setName("Kargo Ücreti");
                shippingItem.setCategory1("Ekstra Hizmet");
                shippingItem.setCategory2("Kargo");
                shippingItem.setItemType(BasketItemType.PHYSICAL.name()); // Sanal olmasın
                shippingItem.setPrice(req.getShippingPrice());

                basketItems.add(shippingItem);
            }

            // ProductId ve quantity bilgisi geliyor ürünün stoğunu renk ve bedene göre veritabanından düşecek

// Tüm ürünlerin toplam fiyatını hesapla
            BigDecimal totalPrice = basketItems.stream()
                    .map(BasketItem::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

// Toplam fiyatı request içine ekleyelim
            request.setPrice(totalPrice);
            request.setPaidPrice(totalPrice);
            request.setBasketItems(basketItems);

            ThreedsInitialize threedsInitialize =  ThreedsInitialize.create(request,options);

            try {
                if(threedsInitialize.getStatus().equals("success")) {
                    orderService.save(new OrderDto(
                            new BuyerDto(
                                    req.getBuyer().getName(),
                                    req.getBuyer().getSurname(),
                                    req.getBuyer().getGsmNumber(),
                                    req.getBuyer().getEmail(),
                                    req.getBuyer().getIp(),
                                    req.getBuyer().getIdentityNumber(),
                                    req.getBuyer().getLastLoginDate(),
                                    req.getBuyer().getRegistrationDate(),
                                    req.getBuyer().getRegistrationAddress()
                            ),
                            new AddressDto(
                                    req.getShippingAddress().getContactName(),
                                    req.getShippingAddress().getCity(),
                                    req.getShippingAddress().getState(),
                                    req.getShippingAddress().getCountry(),
                                    req.getShippingAddress().getAddress(),
                                    req.getShippingAddress().getStreet(),
                                    req.getShippingAddress().getZipCode()
                            ),
                            new AddressDto(
                                    req.getBillingAddress().getContactName(),
                                    req.getBillingAddress().getCity(),
                                    req.getBillingAddress().getState(),
                                    req.getBillingAddress().getCountry(),
                                    req.getBillingAddress().getAddress(),
                                    req.getBillingAddress().getStreet(),
                                    req.getBillingAddress().getZipCode()
                            ),
                            req.getBasketItems().stream().map(bi -> new BasketItemDto(
                                    bi.getName(),
                                    bi.getCategory1(),
                                    bi.getCategory2(),
                                    bi.getPrice(),
                                    bi.getQuantity(),
                                    bi.getSize(),
                                    bi.getColor(),
                                    bi.getStockSizeId(),
                                    bi.getStockCode(),
                                    bi.getImage()
                            )).toList(),
                            totalPrice,
                            OrderStatus.PENDING,
                            LocalDateTime.now(),
                            threedsInitialize.getPaymentId(),
                            null
                    ));
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e.getMessage());
            }

            return threedsInitialize;

        } catch (RuntimeException e) {
            throw new RuntimeException("Error creating payment request", e);
        }
    }

    @Transactional
    public ThreedsPayment completePayment(String paymentId, String conversationId) {
        try {
            CreateThreedsPaymentRequestV2 threedsRequest = new CreateThreedsPaymentRequestV2();
            threedsRequest.setPaymentId(paymentId);
            threedsRequest.setConversationId(conversationId);
            threedsRequest.setLocale(Locale.TR.getValue());

            // 3D Secure Ödeme Tamamlama
            ThreedsPayment threedsPayment = ThreedsPayment.create(threedsRequest, options);

            if(threedsPayment.getStatus().equals("success")) {
               OrderDto order = orderService.changeOrderStatusAndReturnOrder(paymentId); // set payment status paid
                order.setStatus(OrderStatus.PAID);
                for (BasketItemDto bd: order.getBasketItems()) {
                    productService.reduceStock(bd.getStockSizeId(), bd.getQuantity());
                }
                messagingTemplate.convertAndSend("/topic/orders", order);
            }
            return threedsPayment;

        } catch (RuntimeException e) {
            throw new RuntimeException("Error completing 3D Secure payment", e);
        }
    }

}
