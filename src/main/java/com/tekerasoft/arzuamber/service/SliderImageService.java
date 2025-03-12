package com.tekerasoft.arzuamber.service;

import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.model.SliderImage;
import com.tekerasoft.arzuamber.repository.SliderImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SliderImageService {

    private final SliderImageRepository sliderImageRepository;
    private final FileService fileService;

    public SliderImageService(SliderImageRepository sliderImageRepository, FileService fileService) {
        this.sliderImageRepository = sliderImageRepository;
        this.fileService = fileService;
    }

    public List<SliderImage> getAllSliderImages(String lang) {
        return sliderImageRepository.findAllByLangIgnoreCase(lang);
    }

    public ApiResponse<?> createSliderImage(String lang,List<MultipartFile> files) {
        try {
            for (MultipartFile file : files) {
               String imageUrl = fileService.fileUpload(file);
               sliderImageRepository.save(new SliderImage(
                       imageUrl,
                       lang,
                       null
               ));
            }
            return new ApiResponse<>("Slider Images Updated",null,true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ApiResponse<?> deleteSliderImage(String id) {
        try {
            Optional<SliderImage> sliderImage = sliderImageRepository.findById(UUID.fromString(id));
            sliderImage.ifPresent(image -> fileService.deleteFile(image.getUrl()));
            sliderImageRepository.deleteById(UUID.fromString(id));
            return new ApiResponse<>("Slider Images Deleted",null,true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
