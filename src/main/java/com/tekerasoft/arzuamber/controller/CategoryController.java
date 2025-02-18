package com.tekerasoft.arzuamber.controller;

import com.tekerasoft.arzuamber.dto.CategoryDto;
import com.tekerasoft.arzuamber.dto.request.CreateCategoryRequest;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories(@RequestParam String lang) {
        return ResponseEntity.ok(categoryService.getAllCategories(lang));
    }
}
