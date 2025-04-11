package com.tekerasoft.arzuamber.service;

import com.tekerasoft.arzuamber.dto.CommentDto;
import com.tekerasoft.arzuamber.dto.RateDto;
import com.tekerasoft.arzuamber.dto.request.CreateCommentRequest;
import com.tekerasoft.arzuamber.dto.request.ReplyRequest;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.model.*;
import com.tekerasoft.arzuamber.repository.CommentRepository;
import com.tekerasoft.arzuamber.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final RateService rateService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    public CommentService(CommentRepository commentRepository, ProductRepository productRepository,
                          FileService fileService, RateService rateService, NotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
        this.commentRepository = commentRepository;
        this.productRepository = productRepository;
        this.fileService = fileService;
        this.rateService = rateService;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public ApiResponse<?> addCommentToProduct(String productId, List<MultipartFile> images, CreateCommentRequest req) {
        try {
            Product product = productRepository.findById(UUID.fromString(productId))
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // 1. Görselleri yükle
            List<String> urls = new ArrayList<>();
            if (images != null && !images.isEmpty()) {
                for (MultipartFile image : images) {
                    String fileName = fileService.fileUpload(image);
                    urls.add(fileName);
                }
            }

            // 2. Comment'i oluştur (rate henüz null olacak)
            Comment newComment = new Comment(
                    new ArrayList<>(),  // content'ler sonra eklenecek
                    urls,
                    product,
                    LocalDateTime.now(),
                    false,
                    null,
                    null
            );

            // 3. Content objesini oluştur ve Comment ile ilişkilendir
            Content content = new Content(
                    req.getContent().getUserName(),
                    req.getContent().getMessage(),
                    LocalDateTime.now(),
                    newComment,
                    null
            );
            newComment.getContent().add(content);

            // 4. Rate objesini oluştur, Comment'e bağla
            Rate rate = rateService.createRate(
                    product,
                    newComment,
                    new RateDto(
                            null,
                            req.getRate().getUserName(),
                            req.getRate().getUserId(),
                            req.getRate().getRate()
                    )
            );

            // 5. Hem comment hem de rate'yi birbirine bağla
            newComment.setRate(rate);

            // 6. Comment'i (ve cascade ile Rate/Content’i) kaydet
            commentRepository.save(newComment);
            Notification notification = notificationService.saveNotification(new Notification(
                    NotificationType.COMMENT,
                    "New Comment on"+" "+product.getName()+" "+"from"+ " "+ req.getRate().getUserName(),
                    req.getContent().getMessage(),
                    LocalDateTime.now(),
                    true,
                    null
            ));
            messagingTemplate.convertAndSend("/topic/notification", notification);
            messagingTemplate.convertAndSend("/topic/visitors", notification);
            return new ApiResponse<>("Yorumunuz kontrol edilince yayınlanacaktır, teşekkür ederiz :)", null, true);

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<CommentDto> getComments(String productId) {
        return commentRepository.findAll().stream().map(CommentDto::toDto).toList();
    }

    public ApiResponse<?> deleteComment(String commentId) {
        try {
            Comment comment = commentRepository.findById(UUID.fromString(commentId)).orElseThrow();
            for (String img : Objects.requireNonNull(comment.getImages())) {
                fileService.deleteFile(img);
            }
            commentRepository.delete(comment);
            return new ApiResponse<>("Yorum Silindi", null, true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ApiResponse<?> changeCommentStatus(String commentId, boolean status) {
        try {
            Comment comment = commentRepository.findById(UUID.fromString(commentId)).orElseThrow();
            comment.setActive(status);
            commentRepository.save(comment);
            return new ApiResponse<>("Yorum aktif yapıldı", null, true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ApiResponse<?> replyComment(ReplyRequest req) {
        try {
            Comment comment = commentRepository.findById(UUID.fromString(req.getCommentId())).orElseThrow();
            comment.getContent().add(new Content(
                    req.getUserName(),
                    req.getMessage(),
                    LocalDateTime.now(),
                    comment,
                    null
            ));
            commentRepository.save(comment);
            return new ApiResponse<>("Yanıt iletildi.", null, true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}
