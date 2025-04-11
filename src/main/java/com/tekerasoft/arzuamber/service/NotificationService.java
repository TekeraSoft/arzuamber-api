package com.tekerasoft.arzuamber.service;

import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.model.Notification;
import com.tekerasoft.arzuamber.repository.NotificationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final PagedResourcesAssembler<Notification> pagedResourcesAssembler;

    public NotificationService(NotificationRepository notificationRepository,
                               PagedResourcesAssembler<Notification> pagedResourcesAssembler) {
        this.notificationRepository = notificationRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    public PagedModel<EntityModel<Notification>> getAllNotifications(int page, int size) {
        return pagedResourcesAssembler.toModel(notificationRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page,size)));
    }

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public void deactivateAllNotifications() {
        notificationRepository.deactivateAllNotifications();
    }

    public ApiResponse<?> deleteNotification(Notification notification) {
        try {
            notificationRepository.delete(notification);
            return new ApiResponse<>("Notification Deleted", null, true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
