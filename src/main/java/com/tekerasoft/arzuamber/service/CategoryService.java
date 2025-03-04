package com.tekerasoft.arzuamber.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tekerasoft.arzuamber.dto.CategoryDto;
import com.tekerasoft.arzuamber.dto.request.CreateCategoryRequest;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    public CategoryService(CategoryRepository categoryRepository, FileService fileService) {
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
    }


    public ApiResponse<?> createCategory(String categoriesJson, List<MultipartFile> images) {
        try {
            // JSON verisini nesneye çevir
            ObjectMapper objectMapper = new ObjectMapper();
            List<CreateCategoryRequest> categoryRequests = objectMapper.readValue(categoriesJson,
                    new TypeReference<List<CreateCategoryRequest>>() {});

                for(CreateCategoryRequest catReq : categoryRequests){
                    for(MultipartFile image : images){
                        String imageUrl = fileService.fileUpload(image);
                        categoryRepository.save(CategoryDto.createCategoryEntity(catReq, imageUrl));
                    }
                }
            } catch (RuntimeException | JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        return new ApiResponse<>("Category Updated", null, true);
    }

    public List<CategoryDto> getAllCategories(String lang) {
        return categoryRepository.findByLangIgnoreCase(lang).stream().map(CategoryDto::toDto).toList();
    }
}
