package com.tekerasoft.arzuamber.repository;

import com.tekerasoft.arzuamber.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByLangIgnoreCase(String lang);
}
