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


    public ApiResponse<?> createCategory(List<CreateCategoryRequest> categoryRequest) {
        try {
            for(CreateCategoryRequest req : categoryRequest) {
                categoryRepository.save(CategoryDto.createCategoryEntity(req));
            }
            return new ApiResponse<>("Category updated", null, true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<CategoryDto> getAllCategories(String lang) {
        return categoryRepository.findByLangIgnoreCase(lang).stream().map(CategoryDto::toDto).toList();
    }
}
