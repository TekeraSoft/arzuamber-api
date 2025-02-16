package com.tekerasoft.arzuamber.service;

import com.tekerasoft.arzuamber.dto.CategoryDto;
import com.tekerasoft.arzuamber.dto.request.CreateCategoryRequest;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ApiResponse<?> createCategory(CreateCategoryRequest reqParam) {
        try {
            categoryRepository.save(CategoryDto.createCategoryEntity(reqParam));
            return new ApiResponse<>("Category created", null, true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ApiResponse<?> updateCategory(CreateCategoryRequest categoryRequest) {
        try {
            categoryRepository.save(CategoryDto.createCategoryEntity(categoryRequest));
            return new ApiResponse<>("Category updated", null, true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryDto::toDto).toList();
    }
}
