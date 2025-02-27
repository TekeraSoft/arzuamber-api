package com.tekerasoft.arzuamber.repository;

import com.tekerasoft.arzuamber.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ColorRepository extends JpaRepository<Color, UUID> {
    List<Color> findByLangIgnoreCase(String lang);
}
