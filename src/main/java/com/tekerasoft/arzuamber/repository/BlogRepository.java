package com.tekerasoft.arzuamber.repository;

import com.tekerasoft.arzuamber.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BlogRepository extends JpaRepository<Blog, UUID> {
    Page<Blog> findByLangIgnoreCase(String lang, Pageable pageable);
    Optional<Blog> findByLangIgnoreCaseAndSlugIgnoreCase(String lang, String slug);
}
