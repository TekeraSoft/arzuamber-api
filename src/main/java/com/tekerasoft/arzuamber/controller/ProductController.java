package com.tekerasoft.arzuamber.controller;

import com.tekerasoft.arzuamber.dto.ProductDto;
import com.tekerasoft.arzuamber.dto.request.AddToCartRequest;
import com.tekerasoft.arzuamber.model.Color;
import com.tekerasoft.arzuamber.service.ColorService;
import com.tekerasoft.arzuamber.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/product")
public class ProductController {

    private final ProductService productService;
    private final ColorService colorService;

    public ProductController(ProductService productService, ColorService colorService) {
        this.productService = productService;
        this.colorService = colorService;
    }

    @GetMapping("/products")
    public ResponseEntity<PagedModel<EntityModel<ProductDto>>> getAllProducts(@RequestParam String lang, @RequestParam int page,
                                                                              @RequestParam int size) {
        return ResponseEntity.ok(productService.getAllProductsByActive(lang, page, size));
    }

    @GetMapping("/get-all-new-season")
    public ResponseEntity<List<ProductDto>> getAllNewSeasonProduct(@RequestParam String lang, @RequestParam int page,
                                                                   @RequestParam int size) {
        return ResponseEntity.ok(productService.getAllNewSeasonProduct(lang, page, size));
    }

    @GetMapping("/get-all-populate")
    public ResponseEntity<List<ProductDto>> getAllPopulateProduct(@RequestParam String lang, @RequestParam int page,
                                                                  @RequestParam int size) {
        return ResponseEntity.ok(productService.getAllPopulateProduct(lang, page, size));
    }

    @GetMapping("/get-product")
    public ResponseEntity<ProductDto> getProduct(@RequestParam String lang, @RequestParam String slug) {
        return ResponseEntity.ok(productService.getProductBySlug(lang, slug));
    }

    @GetMapping("/filter-product")
    public ResponseEntity<PagedModel<EntityModel<ProductDto>>> getProductFilter(@RequestParam(required = false) String lang,
                                                                                @RequestParam(required = false) String size,
                                                                                @RequestParam(required = false) String color,
                                                                                @RequestParam(required = false) String category,
                                                                                @RequestParam(required = false) String length,
                                                                                @RequestParam(required = false) String sortDirection,
                                                                                @RequestParam(required = false) Boolean onlyDiscounted,
                                                                                @RequestParam int page, @RequestParam int pageSize) {
        return ResponseEntity.ok(productService.filterProducts(lang, size, color, category, length, sortDirection, onlyDiscounted, page, pageSize));
    }

    @GetMapping("/search-product")
    public ResponseEntity<List<ProductDto>> searchProduct(@RequestParam("searchTerm") String searchTerm) {
        return ResponseEntity.ok(productService.searchByNameOrStockCode(searchTerm));
    }

    @GetMapping("/get-all-colors")
    public ResponseEntity<List<Color>> getAllColors(@RequestParam String lang) {
        return ResponseEntity.ok(colorService.getAllColors(lang));
    }

    @PostMapping("/add-to-cart")
    public void addToCart(@RequestBody AddToCartRequest req) {
        productService.addToCart(req);
    }

}
