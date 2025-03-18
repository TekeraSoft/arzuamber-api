package com.tekerasoft.arzuamber.controller;

import com.tekerasoft.arzuamber.dto.*;
import com.tekerasoft.arzuamber.dto.request.*;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.model.*;
import com.tekerasoft.arzuamber.service.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
    private final ColorService colorService;
    private final OrderService orderService;
    private final BlogService blogService;
    private final ContactService contactService;
    private final SliderImageService sliderImageService;

    public AdminController(ProductService productService, CategoryService categoryService, ColorService colorService,
                           OrderService orderService, BlogService blogService, ContactService contactService,
                           SliderImageService sliderImageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.colorService = colorService;
        this.orderService = orderService;
        this.blogService = blogService;
        this.contactService = contactService;
        this.sliderImageService = sliderImageService;
    }

    //public PagedModel<EntityModel<OrderDto>> getAllOrders(Payload)

    @PostMapping("/create-product")
    public ResponseEntity<ApiResponse<?>> createProduct(@RequestParam String lang,
                                                        @RequestPart("data") CreateProductRequest createProductRequest,
                                                        @RequestPart("images") List<MultipartFile> images) {
        return ResponseEntity.ok(productService.createProduct(lang, createProductRequest, images));
    }

    @PutMapping("/update-product")
    public ResponseEntity<ApiResponse<?>> updateProduct(@RequestParam String lang,
                                                        @RequestParam("data") String dataJson,
                                                        @RequestPart(value = "images", required = false)
                                                        List<MultipartFile> images) throws RuntimeException {
        try {
            return ResponseEntity.ok(productService.updateProduct(lang, dataJson, images));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @PatchMapping("/update-product-active")
    public ResponseEntity<ApiResponse<?>> updateProductActive(@RequestParam("productId") String productId,
                                                              @RequestParam("active") boolean active) {
        return ResponseEntity.ok(productService.changeProductActive(productId, active));
    }

    @GetMapping("/get-all-product")
    public ResponseEntity<PagedModel<EntityModel<ProductDto>>> getAllProduct(@RequestParam String lang,
                                                                             @RequestParam int page,
                                                                             @RequestParam int size) {
        return ResponseEntity.ok(productService.getAllProducts(lang, page, size));
    }

    @GetMapping("/get-product")
    public ResponseEntity<ProductDto> getProduct(@RequestParam String id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @DeleteMapping("/delete-order")
    public ResponseEntity<ApiResponse<?>> deleteOrder(@RequestParam String id) {
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }

    @GetMapping("/get-all-order")
    public ResponseEntity<PagedModel<EntityModel<OrderDto>>> getAllOrder(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(orderService.getAllOrders(page,size));
    }

    @PatchMapping("/change-order-status")
    public ResponseEntity<ApiResponse<?>> changeOrderStatus(@RequestParam String orderId, @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.changeOrderStatusWithAdmin(orderId, status));
    }

    @DeleteMapping("/delete-product")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@RequestParam String id) {
        return ResponseEntity.ok(productService.deleteProductById(id));
    }

    @PostMapping(value = "create-category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createCategory(@RequestPart("categoriesJson") String categoriesJson,
                                         @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return categoryService.createCategory(categoriesJson, images);
    }

    @GetMapping("/get-all-category")
    public ResponseEntity<List<CategoryDto>> getAllCategory(@RequestParam String lang) {
        return ResponseEntity.ok(categoryService.getAllCategories(lang));
    }

    @PutMapping("update-price-by-percentage")
    public ResponseEntity<ApiResponse<?>> updatePriceByPercentage(@RequestBody PriceUpdatePercentageRequest percentageRequest) {
        return ResponseEntity.ok(productService.updateAllToursPrice(percentageRequest.getPercentage()));
    }

    @PostMapping("/create-color")
    public ResponseEntity<ApiResponse<?>> createColor(@RequestBody Color color) {
        return ResponseEntity.ok(colorService.saveColor(color));
    }

    @GetMapping("/get-all-colors")
    public ResponseEntity<List<Color>> getAllColors(@RequestParam String lang) {
        return ResponseEntity.ok(colorService.getAllColors(lang));
    }

    @DeleteMapping("/delete-color")
    public ResponseEntity<ApiResponse<?>> deleteColor(@RequestParam String id) {
        return ResponseEntity.ok(colorService.deleteColor(id));
    }

    @PostMapping("/create-blog")
    public ResponseEntity<ApiResponse<?>> createBlog(@RequestParam String lang,
                                                     @RequestPart("values") CreateBlogRequest req,
                                                     @RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok(blogService.createBlog(lang, req, image));
    }

    @GetMapping("/get-all-blog")
    public ResponseEntity<PagedModel<EntityModel<BlogDto>>> getAllBlogs(@RequestParam String lang, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(blogService.getAllBlogs(lang, page, size));
    }

    @DeleteMapping("/delete-blog")
    public ResponseEntity<ApiResponse<?>> deleteBlog(@RequestParam String id) {
        return ResponseEntity.ok(blogService.deleteBlog(id));
    }

    @GetMapping("/get-all-contact")
    public ResponseEntity<PagedModel<EntityModel<ContactDto>>> getAllContact(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(contactService.getAllContacts(page, size));
    }

    @DeleteMapping("/delete-contact")
    public ResponseEntity<ApiResponse<?>> deleteContact(@RequestParam String id) {
        return ResponseEntity.ok(contactService.deleteContact(id));
    }

    @GetMapping("/get-all-slider")
    public ResponseEntity<List<SliderImage>> getAllSlider(@RequestParam String lang) {
        return ResponseEntity.ok(sliderImageService.getAllSliderImages(lang));
    }

    @PostMapping("/create-slider")
    public ResponseEntity<ApiResponse<?>> createSlider(@RequestParam String lang, List<MultipartFile> images) {
        return ResponseEntity.ok(sliderImageService.createSliderImage(lang, images));
    }

    @DeleteMapping("/delete-slider")
    public ResponseEntity<ApiResponse<?>> deleteSlider(@RequestParam String id) {
        return ResponseEntity.ok(sliderImageService.deleteSliderImage(id));
    }
}
