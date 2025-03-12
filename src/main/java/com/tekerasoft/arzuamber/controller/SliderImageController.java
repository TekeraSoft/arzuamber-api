package com.tekerasoft.arzuamber.controller;

import com.tekerasoft.arzuamber.model.SliderImage;
import com.tekerasoft.arzuamber.service.SliderImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/slider")
public class SliderImageController {
    private final SliderImageService sliderImageService;

    public SliderImageController(SliderImageService sliderImageService) {
        this.sliderImageService = sliderImageService;
    }

    public ResponseEntity<List<SliderImage>> findAll(String lang) {
        return ResponseEntity.ok(sliderImageService.getAllSliderImages(lang));
    }
}
