package com.tekerasoft.arzuamber.service;

import com.tekerasoft.arzuamber.dto.FashionCollectionDto;
import com.tekerasoft.arzuamber.dto.request.CreateFashionCollectionRequest;
import com.tekerasoft.arzuamber.dto.request.UpdateFashionCollectionRequest;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.model.FashionCollection;
import com.tekerasoft.arzuamber.model.Product;
import com.tekerasoft.arzuamber.repository.FashionCollectionRepository;
import com.tekerasoft.arzuamber.utils.SlugGenerator;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class FashionCollectionService {
    private final FashionCollectionRepository fashionCollectionRepository;
    private final ProductService productService;
    private final FileService fileService;

    public FashionCollectionService(FashionCollectionRepository fashionCollectionRepository,
                                    ProductService productService, FileService fileService) {
        this.fashionCollectionRepository = fashionCollectionRepository;
        this.productService = productService;
        this.fileService = fileService;
    }

    public ApiResponse<?> createFashionCollection(CreateFashionCollectionRequest req) {
        Set<Product> productList = new LinkedHashSet<>(); {
        };
        req.getProducts().forEach(product -> {
            productList.add(productService.getProductById(product));
        });
        FashionCollection collection = new FashionCollection();
        String imagePath = fileService.fileUpload(req.getImage());
        collection.setImage(imagePath);
        collection.setSlug(SlugGenerator.generateSlug(req.getCollectionName()));
        collection.setProducts(productList);
        collection.setCollectionName(req.getCollectionName());
        collection.setDescription(req.getDescription());
        collection.setActive(true);
        fashionCollectionRepository.save(collection);
        return new ApiResponse<>("Collection Created",null, true);
    }

    @Transactional                          // tek transaction
    public ApiResponse<?> updateFashionCollection(UpdateFashionCollectionRequest req) {

        FashionCollection collection = fashionCollectionRepository.findById(
                        UUID.fromString(req.getId()))
                .orElseThrow(() -> new RuntimeException("Fashion Collection not found"));

        /* 1️⃣ Koleksiyonu yerinde temizle */
        collection.getProducts().clear();

        /* 2️⃣ Yeni Product’ları ekle */
        req.getProducts().forEach(productId -> {
            Product p = productService.getProductById(productId);
            collection.getProducts().add(p);       // ⇦ addAll da olur
        });

        /* 3️⃣ Diğer alanlar */
        if (!req.getImage().isEmpty()) {
            fileService.deleteFile(collection.getImage());
            String path = fileService.fileUpload(req.getImage());
            collection.setImage(path);
        }
        collection.setCollectionName(req.getCollectionName());
        collection.setSlug(req.getCollectionName());
        collection.setDescription(req.getDescription());

        /* 4️⃣ Kaydet (flush otomatik) */
        fashionCollectionRepository.save(collection);

        return new ApiResponse<>("Collection Updated", null, true);
    }

    public Page<FashionCollectionDto> getAllFashionCollection(Pageable pageable) {
        return fashionCollectionRepository.findActiveCollections(pageable).map(FashionCollectionDto::toDto);
    }

    public FashionCollectionDto getFashionCollection(String id) {
        return fashionCollectionRepository.findById(UUID.fromString(id))
                .map(FashionCollectionDto::toDto).orElseThrow(() -> new RuntimeException("Fashion Collection not found"));
    }

    public ApiResponse<?> deleteFashionCollection(String id) {
        FashionCollection collection = fashionCollectionRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Fashion Collection not found"));
        fileService.deleteFile(collection.getImage());
        fashionCollectionRepository.delete(collection);
        return new ApiResponse<>("Collection Deleted", null, true);
    }
}
