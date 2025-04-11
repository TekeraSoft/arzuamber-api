package com.tekerasoft.arzuamber.repository;

import com.tekerasoft.arzuamber.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
}
