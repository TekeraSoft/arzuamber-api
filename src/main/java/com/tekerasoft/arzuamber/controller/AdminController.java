package com.tekerasoft.arzuamber.controller;

import com.tekerasoft.arzuamber.dto.CategoryDto;
import com.tekerasoft.arzuamber.dto.ProductDto;
import com.tekerasoft.arzuamber.dto.request.CreateCategoryRequest;
import com.tekerasoft.arzuamber.dto.request.CreateProductRequest;
import com.tekerasoft.arzuamber.dto.request.PriceUpdatePercentageRequest;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.service.CategoryService;
import com.tekerasoft.arzuamber.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/api/admin")
public class AdminController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @PostMapping("/create-product")
    public ResponseEntity<ApiResponse<?>> createProduct(@RequestParam String lang,
                                                        @RequestPart("data") CreateProductRequest createProductRequest,
                                                        @RequestPart("images") List<MultipartFile> images) {
        System.out.println(createProductRequest);
        return ResponseEntity.ok(productService.createProduct(lang,createProductRequest,images));
    }

    @DeleteMapping("/delete-product")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@RequestParam String id) {
        return ResponseEntity.ok(productService.deleteProductById(id));
    }

    @PostMapping("create-category")
    public ApiResponse<?> createCategory(@RequestBody List<CreateCategoryRequest> createCategoryRequest) {
        return categoryService.createCategory(createCategoryRequest);
    }

    @GetMapping("/get-all-category")
    public ResponseEntity<List<CategoryDto>> getAllCategory(@RequestParam String lang) {
        return ResponseEntity.ok(categoryService.getAllCategories(lang));
    }

    @PutMapping("/update-product")
    public ResponseEntity<ApiResponse<?>> updateProduct(@RequestBody ProductDto product) {
        return ResponseEntity.ok(productService.updateProduct(product));
    }

    @GetMapping("/get-all-product")
    public ResponseEntity<List<ProductDto>> getAllProduct(@RequestParam String lang,
                                                          @RequestParam int page,
                                                          @RequestParam int size) {
        return ResponseEntity.ok(productService.getAllProducts(lang,page, size));
    }

    @PutMapping("update-price-by-percentage")
    public ResponseEntity<ApiResponse<?>> updatePriceByPercentage(@RequestBody PriceUpdatePercentageRequest percentageRequest) {
        return ResponseEntity.ok(productService.updateAllToursPrice(percentageRequest.getPercentage()));
    }

}
