package com.tekerasoft.arzuamber.service;

import com.tekerasoft.arzuamber.dto.BlogDto;
import com.tekerasoft.arzuamber.dto.request.CreateBlogRequest;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.model.Blog;
import com.tekerasoft.arzuamber.repository.BlogRepository;
import com.tekerasoft.arzuamber.utils.SlugGenerator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final FileService fileService;
    private final PagedResourcesAssembler<BlogDto> pagedResourcesAssembler;

    public BlogService(BlogRepository blogRepository, FileService fileService,
                       PagedResourcesAssembler<BlogDto> pagedResourcesAssembler) {
        this.blogRepository = blogRepository;
        this.fileService = fileService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    public ApiResponse<?> createBlog(String lang, CreateBlogRequest blogReq, MultipartFile image) {
        try {
            String imgUrl = fileService.fileUpload(image);
            Blog blog = new Blog(blogReq.getTitle(), SlugGenerator.generateSlug(blogReq.getTitle()), blogReq.getCategory(),imgUrl, blogReq.getContent(),lang);
            blogRepository.save(blog);
            return new ApiResponse<>("Blog Created",null,true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public PagedModel<EntityModel<BlogDto>> getAllBlogs(String lang,int page, int size) {
        return pagedResourcesAssembler.toModel(blogRepository.findByLangIgnoreCase(lang, PageRequest.of(page,size))
                .map(BlogDto::toDto));
    }

    public BlogDto getBlogBySlug(String lang,String slug) {
        return blogRepository.findByLangIgnoreCaseAndSlugIgnoreCase(lang,slug)
                .map(BlogDto::toDto).get();
    }

    public ApiResponse<?> deleteBlog(String id) {
        try {
            blogRepository.deleteById(UUID.fromString(id));
            return new ApiResponse<>("Blog Deleted",null,true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
