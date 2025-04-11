package com.tekerasoft.arzuamber.service;

import com.tekerasoft.arzuamber.dto.ProductDto;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.exception.AlreadyFavoriteException;
import com.tekerasoft.arzuamber.model.*;
import com.tekerasoft.arzuamber.repository.FavoriteProductRepository;
import com.tekerasoft.arzuamber.repository.ProductRepository;
import com.tekerasoft.arzuamber.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FavoriteProductService {
    private final UserRepository userRepository;
    private final FavoriteProductRepository favoriteProductRepository;
    private final ProductRepository productRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    public FavoriteProductService(UserRepository userRepository, FavoriteProductRepository favoriteProductRepository, ProductRepository productRepository, NotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
        this.userRepository = userRepository;
        this.favoriteProductRepository = favoriteProductRepository;
        this.productRepository = productRepository;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    public ApiResponse<?> addFavorite(UUID userId, UUID productId) {
        try {
            User user = userRepository.findById(userId).orElseThrow();
            Product product = productRepository.findById(productId).orElseThrow();

            FavoriteProduct existing = favoriteProductRepository.findByUserIdAndProductId(userId, productId);
            if (existing != null) {
                return new ApiResponse<>("Ürün Zaten Favorilerinizde",null,false);
            } else {
                FavoriteProduct favorite = new FavoriteProduct(user, product, LocalDateTime.now(),null);
                favoriteProductRepository.save(favorite);

                Notification notification = new Notification(
                        NotificationType.FAV,
                        user.getFirstName() + " " + user.getLastName() + " add to favorite " + product.getName(),
                        "",
                        LocalDateTime.now(),
                        true,
                        null
                );

                Notification savedNotification = notificationService.saveNotification(notification);
                messagingTemplate.convertAndSend("/topic/visitors", savedNotification);

                return new ApiResponse<>("Ürün Favorilere Eklendi", null, true);
            }

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ApiResponse<?> removeFavorite(String userId, String productId) {
        FavoriteProduct favorite = favoriteProductRepository.findByUserIdAndProductId(UUID.fromString(userId), UUID.fromString(productId));
        if (favorite == null) {
            throw new RuntimeException("Favori ürün bulunamadı");
        }
        favoriteProductRepository.delete(favorite);
        return new ApiResponse<>("Ürün favorilerden kaldırıldı.",null,true);
    }

    public List<ProductDto> getFavoritesForUser(String userId) {
        return favoriteProductRepository.findByUserId(UUID.fromString(userId))
                .stream()
                .map(fav -> ProductDto.toDto(fav.getProduct()))
                .collect(Collectors.toList());
    }

}
