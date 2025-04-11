package com.tekerasoft.arzuamber.repository;

import com.tekerasoft.arzuamber.model.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Page<Notification> findAllByOrderByCreatedAtDesc(Pageable pageable);
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isActive = false")
    void deactivateAllNotifications();
}
