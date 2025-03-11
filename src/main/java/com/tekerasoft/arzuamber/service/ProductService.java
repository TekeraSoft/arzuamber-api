package com.tekerasoft.arzuamber.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tekerasoft.arzuamber.dto.ProductDto;
import com.tekerasoft.arzuamber.dto.request.CreateProductRequest;
import com.tekerasoft.arzuamber.dto.request.UpdateProductRequest;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.exception.ProductException;
import com.tekerasoft.arzuamber.exception.ProductNotFoundException;
import com.tekerasoft.arzuamber.model.ColorSize;
import com.tekerasoft.arzuamber.model.Product;
import com.tekerasoft.arzuamber.model.StockSize;
import com.tekerasoft.arzuamber.repository.ProductRepository;
import com.tekerasoft.arzuamber.utils.SlugGenerator;
import com.tekerasoft.arzuamber.utils.StockCodeGenerator;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final PagedResourcesAssembler<ProductDto> pagedResourcesAssembler;

    public ProductService(ProductRepository productRepository, FileService fileService,
                          PagedResourcesAssembler<ProductDto> pagedResourcesAssembler) {
        this.productRepository = productRepository;
        this.fileService = fileService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
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
            List<ColorSize> colorSizes = req.getColorSize().stream().map(colorSizeReq -> {
                List<String> imgUrls = imageMap.getOrDefault(colorSizeReq.getColor(), new ArrayList<>());

                return new ColorSize(
                        colorSizeReq.getColor(),
                        colorSizeReq.getStockSize().stream()
                                .map(ss -> new StockSize(ss.getSize(), ss.getStock()))
                                .collect(Collectors.toList()),
                        StockCodeGenerator.generateStockCode(8),
                        imgUrls
                );
            }).collect(Collectors.toList());

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
                            .flatMap(cs -> cs.getStockSize().stream())
                            .mapToInt(StockSize::getStock)
                            .sum(),
                    req.getPurchasePrice()
            );

            productRepository.save(newProduct);
            return new ApiResponse<>("Product Created", null, true);
        } catch (RuntimeException e) {
            throw new ProductException(e.getMessage());
        }
    }

    public ApiResponse<?> changeProductActive(String id, Boolean active) {
        try {
            productRepository.updateIsActive(UUID.fromString(id), active);
            return new ApiResponse<>(active ? "Product Active": "Product Deactive", null, true);
        } catch (RuntimeException e) {
            throw new ProductException(e.getMessage());
        }
    }

    public ProductDto getProduct(String id) {
        return productRepository.findById(UUID.fromString(id))
                .map(ProductDto::toDto)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public PagedModel<EntityModel<ProductDto>> getAllProducts(String lang, int page, int size) {

        return pagedResourcesAssembler.toModel(productRepository.findByLangIgnoreCaseOrderByCreatedAtDesc(lang,PageRequest.of(page,size))
                .map(ProductDto::toDto));
    }

    public PagedModel<EntityModel<ProductDto>> getAllProductsByActive(String lang, int page, int size) {
        return pagedResourcesAssembler.toModel(
                productRepository.findByLangIgnoreCaseAndIsActiveTrueOrderByCreatedAtDesc(lang,PageRequest.of(page,size))
                        .map(ProductDto::toDto)
        );
    }

    public List<ProductDto> getAllNewSeasonProduct(String lang, int page, int size) {
        return productRepository.findByNewSeasonTrueAndLangIgnoreCaseAndIsActiveTrueOrderByCreatedAtDesc(lang,PageRequest.of(page,size))
                .stream().map(ProductDto::toDto).collect(Collectors.toList());
    }

    public List<ProductDto> getAllPopulateProduct(String lang, int page, int size) {
        return productRepository.findByPopulateTrueAndLangIgnoreCaseAndIsActiveTrueOrderByCreatedAtDesc(lang,PageRequest.of(page,size))
                .stream().map(ProductDto::toDto)
                .collect(Collectors.toList());
    }

    public ProductDto getProductBySlug(String lang,String slug) {
        return productRepository.findByLangIgnoreCaseAndSlugIgnoreCase(lang,slug)
                .map(ProductDto::toDto)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
    }

    public ApiResponse<?> updateProduct(String lang, String dataJson, List<MultipartFile> images) {
        try {
            // Verilen JSON verisini 'UpdateProductRequest' sınıfına dönüştürüyoruz
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
            UpdateProductRequest upReq = objectMapper.readValue(dataJson, UpdateProductRequest.class);

            // Ürünü ID'sine göre buluyoruz
            Product product = productRepository.findById(UUID.fromString(upReq.getId()))
                    .orElseThrow(() -> new ProductNotFoundException("Product not found " + upReq.getId()));

            // Silinen resimleri işliyoruz
            List<String> deletedImages = upReq.getDeletedImages();
            if (deletedImages != null && !deletedImages.isEmpty()) {
                for (String url : deletedImages) {
                    try {
                        fileService.deleteFile(url);
                    } catch (Exception e) {
                        System.err.println("Error deleting file: " + e.getMessage());
                    }
                }
            }

            // Yeni resimleri işle
            Map<String, List<String>> imageMap = new HashMap<>();
            if (images != null && !images.isEmpty()) {
                for (MultipartFile image : images) {
                    String colorKey = image.getOriginalFilename().split("_")[0];  // Renk anahtarını çıkarıyoruz
                    String imageUrl = fileService.fileUpload(image);
                    imageMap.computeIfAbsent(colorKey, k -> new ArrayList<>()).add(imageUrl);
                }
            }

            // Mevcut ColorSize ilişkilerini alıyoruz
            Map<String, ColorSize> existingColorSizes = product.getColorSize()
                    .stream()
                    .collect(Collectors.toMap(ColorSize::getColor, Function.identity()));

            // Güncellenmiş ColorSize nesnelerini oluşturuyoruz
            List<ColorSize> updatedColorSizes = upReq.getColorSize().stream().map(colorSizeReq -> {
                ColorSize existingColorSize = existingColorSizes.get(colorSizeReq.getColor());

                // Yeni resimleri işliyoruz, varsa mevcut resimleri de ekliyoruz
                List<String> newImages = new ArrayList<>();
                if (existingColorSize != null && existingColorSize.getImages() != null) {
                    newImages.addAll(existingColorSize.getImages());
                }

                List<String> uploadedImages = imageMap.getOrDefault(colorSizeReq.getColor(), new ArrayList<>());
                newImages.addAll(uploadedImages); // Sonra yeni yüklenen resimleri ekle

                // Silinen resimleri kaldırıyoruz
                newImages.removeAll(deletedImages);

                // Yeni ColorSize nesnesini oluşturuyoruz
                // Burada ColorSize önce oluşturuluyor, sonra StockSize'e ilişkilendiriliyor
                ColorSize updatedColorSize = new ColorSize(
                        colorSizeReq.getColor(),
                        new ArrayList<>(), // İlk başta boş liste ile başlatıyoruz
                        (existingColorSize != null) ? existingColorSize.getStockCode() : StockCodeGenerator.generateStockCode(8),
                        newImages.isEmpty() ? colorSizeReq.getImages() : newImages,
                        (existingColorSize != null) ? existingColorSize.getProduct() : product
                );

                // StockSize'leri ilişkilendiriyoruz
                List<StockSize> stockSizes = colorSizeReq.getStockSize().stream()
                        .map(ss -> new StockSize(ss.getSize(), ss.getStock(), updatedColorSize)) // Burada StockSize'ı oluştururken updatedColorSize'ı kullanıyoruz
                        .collect(Collectors.toList());

                // ColorSize'in StockSize listesini güncelliyoruz
                updatedColorSize.setStockSize(stockSizes);

                return updatedColorSize;
            }).collect(Collectors.toList());

            Product newProduct = new Product(
                    upReq.getName(),
                    SlugGenerator.generateSlug(upReq.getName()),
                    upReq.getPopulate(),
                    upReq.getNewSeason(),
                    upReq.getCategory(),
                    upReq.getSubCategory(),
                    upReq.getDescription(),
                    upReq.getPrice(),
                    lang,
                    upReq.getLength(),
                    updatedColorSizes,
                    updatedColorSizes.stream()
                            .flatMap(cs -> cs.getStockSize().stream())
                            .mapToInt(StockSize::getStock)
                            .sum(),
                    upReq.getPurchasePrice(),
                    upReq.getDiscountPrice(),
                    LocalDateTime.now(),
                    product.getCreatedAt(),
                    UUID.fromString(upReq.getId()),
                    product.isActive()
            );

            productRepository.save(newProduct);
            return new ApiResponse<>("Product Updated", null, true);

        } catch (JsonProcessingException e) {
           throw new RuntimeException(e.getMessage());
        } catch (RuntimeException e) {
            throw new ProductException(e.getMessage());
        }
    }

    public PagedModel<EntityModel<ProductDto>> filterProducts(String lang, String size, String color, String category, String length, int page, int pageSize) {
        // Parametrelerin null olup olmadığını kontrol et
        lang = (lang == null || lang.isEmpty()) ? null : lang;
        size = (size == null || size.isEmpty()) ? null : size;
        color = (color == null || color.isEmpty()) ? null : color;
        category = (category == null || category.isEmpty()) ? null : category;
        length = (length == null || length.isEmpty()) ? null : length;

        // Sorguyu çalıştır ve sayfalanmış sonuçları döndür
        return pagedResourcesAssembler.toModel(
                productRepository.findProductsByFilters(color, size, category, length, lang, PageRequest.of(page, pageSize))
                        .map(ProductDto::toDto)
        );
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

    public void reduceStock(String stockSizeId, int quantity) {
        productRepository.reduceStock(UUID.fromString(stockSizeId),quantity);
    }

    public List<ProductDto> searchByNameOrStockCode(String searchTerm) {
        return productRepository.searchByNameOrStockCode(searchTerm)
                .stream()
                .map(ProductDto::toDto)
                .collect(Collectors.toList());
    }

}
