package com.tekerasoft.arzuamber.repository;

import com.tekerasoft.arzuamber.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContentRepository extends JpaRepository<Content, UUID> {
}
