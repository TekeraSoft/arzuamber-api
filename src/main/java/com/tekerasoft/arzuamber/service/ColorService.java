package com.tekerasoft.arzuamber.service;

import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.model.Color;
import com.tekerasoft.arzuamber.repository.ColorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ColorService {

    private final ColorRepository colorRepository;

    public ColorService(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    public ApiResponse<?> saveColor(Color color) {
        try {
            colorRepository.save(color);
            return new ApiResponse<>("ColorCreated",null,true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Color> getAllColors(String lang) {
        return colorRepository.findByLangIgnoreCase(lang);
    }

    public ApiResponse<?> deleteColor(String colorId) {
        try {
            colorRepository.deleteById(UUID.fromString(colorId));
            return new ApiResponse<>("Color Deleted",null,true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
