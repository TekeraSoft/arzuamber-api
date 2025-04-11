package com.tekerasoft.arzuamber.controller;

import com.tekerasoft.arzuamber.dto.CommentDto;
import com.tekerasoft.arzuamber.dto.request.CreateCommentRequest;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/api/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("create-comment")
    public ResponseEntity<ApiResponse<?>> createComment(@RequestParam String productId,
                                                        @RequestPart(value = "data") CreateCommentRequest req,
                                                        @RequestPart(value= "images", required = false) List<MultipartFile> images) {
        return ResponseEntity.ok(commentService.addCommentToProduct(productId,images ,req));
    }

    @GetMapping("get-all-comment")
    public ResponseEntity<List<CommentDto>> getAllComment(@RequestParam String productId) {
        return ResponseEntity.ok(commentService.getComments(productId));
    }

    @DeleteMapping("/delete-comment")
    public ResponseEntity<ApiResponse<?>> deleteComment(@RequestParam String commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }

}
