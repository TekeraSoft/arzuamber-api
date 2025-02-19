package com.tekerasoft.arzuamber.service;

import com.tekerasoft.arzuamber.dto.ProductDto;
import com.tekerasoft.arzuamber.dto.request.CreateProductRequest;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.exception.ProductException;
import com.tekerasoft.arzuamber.exception.ProductNotFoundException;
import com.tekerasoft.arzuamber.model.ColorSize;
import com.tekerasoft.arzuamber.model.Product;
import com.tekerasoft.arzuamber.model.SizeStock;
import com.tekerasoft.arzuamber.repository.ProductRepository;
import com.tekerasoft.arzuamber.utils.SlugGenerator;
import com.tekerasoft.arzuamber.utils.StockCodeGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final FileService fileService;

    public ProductService(ProductRepository productRepository, FileService fileService) {
        this.productRepository = productRepository;
        this.fileService = fileService;
    }

    @Transactional
    public ApiResponse<?> createProduct(String lang, CreateProductRequest req, List<MultipartFile> images) {
        try {
            // 1️⃣ Görselleri `color` değerine göre haritalandır
            Map<String, List<String>> imageMap = new HashMap<>();

            for (MultipartFile image : images) {
                String colorKey = image.getOriginalFilename().split("_")[0]; // "blue_1.jpg" -> "blue"
                String imageUrl = fileService.fileUpload(image);
                imageMap.computeIfAbsent(colorKey, k -> new ArrayList<>()).add(imageUrl);
            }

            // 2️⃣ colorSize içindeki `color` alanına göre görselleri eşleştir
            Set<ColorSize> colorSizes = req.getColorSize().stream().map(colorSizeReq -> {
                List<String> imgUrls = imageMap.getOrDefault(colorSizeReq.getColor(), new ArrayList<>());

                return new ColorSize(
                        colorSizeReq.getColor(),
                        colorSizeReq.getSizeStock().stream()
                                .map(ss -> new SizeStock(ss.getSize(), ss.getStock()))
                                .collect(Collectors.toSet()),
                        StockCodeGenerator.generateStockCode(8),
                        imgUrls
                );
            }).collect(Collectors.toSet());

            // 3️⃣ Yeni Ürün Kaydetme
            Product newProduct = new Product(
                    req.getName(),
                    SlugGenerator.generateSlug(req.getName()),
                    req.getPopulate(),
                    req.getNewSeason(),
                    req.getCategory(),
                    req.getSubCategory(),
                    req.getDescription(),
                    req.getPrice(),
                    lang,
                    req.getLength(),
                    colorSizes,
                    colorSizes.stream()
                            .flatMap(cs -> cs.getSizeStock().stream())
                            .mapToInt(SizeStock::getStock)
                            .sum(),
                    req.getPurchasePrice()
            );

            productRepository.save(newProduct);
            return new ApiResponse<>("Product Created", null, true);
        } catch (RuntimeException e) {
            throw new ProductException(e.getMessage());
        }
    }

    public List<ProductDto> getAllProducts(String lang, int page, int size) {
        return productRepository.findByLangIgnoreCase(lang,PageRequest.of(page,size))
                .stream()
                .map(ProductDto::toDto)
                .collect(Collectors.toList());
    }

    public List<ProductDto> getAllNewSeasonProduct(String lang, int page, int size) {
        return productRepository.findByNewSeasonAndLang(lang,PageRequest.of(page,size))
                .stream().map(ProductDto::toDto)
                .collect(Collectors.toList());
    }

    public ProductDto getProductBySlug(String lang,String slug) {
        return productRepository.findByLangIgnoreCaseAndSlugIgnoreCase(lang,slug)
                .map(ProductDto::toDto)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
    }

    public ApiResponse<?> updateProduct(ProductDto req) {
        try {

            return new ApiResponse<>("Product Updated",null,true);
        } catch (RuntimeException e) {
            throw new ProductException(e.getMessage());
        }
    }

    public List<ProductDto> filterProducts(String lang, String size, String color, String category, String length) {
        lang = (lang.isEmpty()) ? null : lang;
        size = (size.isEmpty()) ? null : size;
        color = (color.isEmpty()) ? null : color;
        category = (category.isEmpty()) ? null : category;
        length = (length.isEmpty()) ? null : length;

        return productRepository.findProductsByFilters(color,size,category,length,lang)
                .stream().map(ProductDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ApiResponse<?> updateAllToursPrice(BigDecimal percentage) {
        try {
            // Tüm turları alıyoruz
            List<Product> tours = productRepository.findAllProductPrices();

            // Oranı hesaplıyoruz
            BigDecimal multiplier = BigDecimal.ONE.add(percentage.divide(BigDecimal.valueOf(100)));

            // Her turun fiyatını güncelliyoruz
            for (Product product : tours) {
                BigDecimal currentPrice = product.getPrice();
                BigDecimal newPrice = currentPrice.multiply(multiplier);
                BigDecimal roundedPrice = newPrice.setScale(0, RoundingMode.HALF_UP);

                // Fiyatı güncelliyoruz
                product.setPrice(roundedPrice);
                // Fiyatı veritabanına kaydediyoruz
                productRepository.updateProductPriceById(roundedPrice, product.getId());
            }

            return new ApiResponse<>(String.format("All tours price updated by %.1f%%", percentage), null, true);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating prices: " + e.getMessage());
        }
    }

    public ApiResponse<?> deleteProductById(String id) {
        try {
            Product product = productRepository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));

           for (ColorSize cp : product.getColorSize()) {
               if (cp.getImages() != null) {
                   for (String imageUrl : cp.getImages()) {
                       fileService.deleteFile(imageUrl);
                   }
               }
           }
            productRepository.delete(product);
            return new ApiResponse<>("Product Deleted", null, true);
        } catch (RuntimeException e) {
            throw new ProductException(e.getMessage());
        }
    }
}
