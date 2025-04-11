package com.tekerasoft.arzuamber.repository;

import com.tekerasoft.arzuamber.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByLangIgnoreCaseOrderByCreatedAtDesc(String lang, Pageable pageable);

    Page<Product> findByLangIgnoreCaseAndIsActiveTrueOrderByCreatedAtDesc(String lang, Pageable pageable);

    Optional<Product> findByLangIgnoreCaseAndSlugIgnoreCase(String lang, String slug);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.isActive = :isActive WHERE p.id = :productId")
    void updateIsActive(@Param("productId") UUID productId, @Param("isActive") boolean isActive);

    //@Query("SELECT t FROM Product t WHERE t.newSeason = true AND t.lang = :lang AND t.isActive = true")
    Page<Product> findByNewSeasonTrueAndLangIgnoreCaseAndIsActiveTrueOrderByCreatedAtDesc(@Param("lang") String lang, Pageable pageable);

    //@Query("SELECT t FROM Product t WHERE t.populate = true AND t.lang = :lang AND t.isActive = true")
    Page<Product> findByPopulateTrueAndLangIgnoreCaseAndIsActiveTrueOrderByCreatedAtDesc(@Param("lang") String lang, Pageable pageable);

    @Query("SELECT t FROM Product t WHERE t.price IS NOT NULL")
    List<Product> findAllProductPrices();

    @Modifying
    @Query("UPDATE Product t SET t.price = :price WHERE t.id = :id")
    void updateProductPriceById(@Param("price") BigDecimal price, @Param("id") UUID id);

    @Modifying
    @Query("UPDATE Product t SET t.price = t.price + :amount")
    void increaseAllProductsPrice(@Param("amount") BigDecimal amount);

    @Modifying
    @Query("UPDATE Product t SET t.price = t.price - :amount")
    int decreaseAllProductsPrice(@Param("amount") BigDecimal amount);

    @Modifying
    @Transactional
    @Query("UPDATE StockSize ss SET ss.stock = ss.stock - :quantity WHERE ss.id = :stockSizeId AND ss.stock >= :quantity")
    void reduceStock(@Param("stockSizeId") UUID stockSizeId, @Param("quantity") int quantity);

    @Query("""
    SELECT DISTINCT p FROM Product p
    LEFT JOIN p.colorSize cs
    LEFT JOIN cs.stockSize ss
    WHERE
        (:color IS NULL OR cs.color = :color)
        AND (:size IS NULL OR ss.size = :size)
        AND (:category IS NULL OR p.category = :category)
        AND (:length IS NULL OR p.length = :length)
        AND (:lang IS NULL OR p.lang = :lang)
        AND (p.isActive = true)
        AND (:onlyDiscounted = false OR (p.discountPrice IS NOT NULL AND p.discountPrice > 0))
""")
    Page<Product> findProductsByFilters(
            @Param("color") String color,
            @Param("size") String size,
            @Param("category") String category,
            @Param("length") String length,
            @Param("lang") String lang,
            @Param("onlyDiscounted") boolean onlyDiscounted,
            Pageable pageable
    );

    @Query("SELECT p FROM Product p " +
            "JOIN p.colorSize cs " +
            "WHERE p.isActive = true AND " +  // 🔥 isActive şartı eklendi
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(cs.stockCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Product> searchByNameOrStockCode(@Param("searchTerm") String searchTerm);

    @Query("SELECT p FROM Product p " +
            "JOIN p.colorSize cs " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(cs.stockCode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> searchAdminByNameOrStockCode(@Param("searchTerm") String searchTerm);
}
