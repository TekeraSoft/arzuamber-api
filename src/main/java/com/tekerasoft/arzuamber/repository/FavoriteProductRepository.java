package com.tekerasoft.arzuamber.repository;

import com.tekerasoft.arzuamber.model.FavoriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, UUID> {
    List<FavoriteProduct> findByUserId(UUID userId);
    FavoriteProduct findByUserIdAndProductId(UUID userId, UUID productId);
}
