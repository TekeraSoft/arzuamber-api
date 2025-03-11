package com.tekerasoft.arzuamber.repository;

import com.tekerasoft.arzuamber.model.Order;
import com.tekerasoft.arzuamber.model.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = :status WHERE o.paymentId = :paymentId")
    void updateOrderStatusByPaymentId(@Param("paymentId") String paymentId, @Param("status") OrderStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :orderId")
    void updateOrderStatus(@Param("orderId") UUID orderId, @Param("status") OrderStatus status);

    @Modifying
    @Transactional
    @Query("SELECT o FROM Order o WHERE o.buyer.email = :email")
    List<Order> findUserOrdersByEmail(String email);

    Optional<Order> findByPaymentId(String paymentId);
}
