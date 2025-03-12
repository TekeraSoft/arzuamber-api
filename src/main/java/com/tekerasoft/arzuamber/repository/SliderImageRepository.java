package com.tekerasoft.arzuamber.repository;

import com.tekerasoft.arzuamber.model.SliderImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SliderImageRepository extends JpaRepository<SliderImage, UUID> {
    List<SliderImage> findAllByLangIgnoreCase(String lang);
}
