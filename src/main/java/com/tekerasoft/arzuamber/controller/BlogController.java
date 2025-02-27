package com.tekerasoft.arzuamber.controller;

import com.tekerasoft.arzuamber.dto.BlogDto;
import com.tekerasoft.arzuamber.service.BlogService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/blog")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/get-all-blog")
    public ResponseEntity<PagedModel<EntityModel<BlogDto>>> getAllBlogs(@RequestParam String lang, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(blogService.getAllBlogs(lang, page, size));
    }

    @GetMapping("/get-blog")
    public ResponseEntity<BlogDto> getBlog(@RequestParam String lang,@RequestParam String slug) {
        return ResponseEntity.ok(blogService.getBlogBySlug(lang,slug));
    }
}
