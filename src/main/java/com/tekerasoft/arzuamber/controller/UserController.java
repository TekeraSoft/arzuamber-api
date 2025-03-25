package com.tekerasoft.arzuamber.controller;

import com.tekerasoft.arzuamber.dto.request.EditUserRequest;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.model.User;
import com.tekerasoft.arzuamber.service.MailService;
import com.tekerasoft.arzuamber.service.OrderService;
import com.tekerasoft.arzuamber.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/api/user")
public class UserController {
    private final UserService userService;
    private final MailService mailService;

    public UserController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<?>> sendChangeMail(@RequestParam String email,
                                                         @RequestParam String oldPassword,
                                                         @RequestParam String password,
                                                         @RequestParam String token) throws MessagingException {
       return ResponseEntity.ok(userService.changePassword(email, oldPassword, password, token));
    }

    @GetMapping("/forgot-password-mail")
    public void forgotPassword(@RequestParam String email) throws MessagingException {
        mailService.sendForgotPasswordMail(email);
    }

    @PostMapping("/change-forgot-password")
    public ResponseEntity<ApiResponse<?>> changeForgotPassword(@RequestParam String mail, @RequestParam String token, @RequestParam String password) {
        return ResponseEntity.ok(userService.forgotPassword(mail,password,token));
    }

    @PatchMapping("/edit-user-details")
    public ResponseEntity<ApiResponse<?>> editUserDetails(@RequestBody EditUserRequest editUserRequest){
        return ResponseEntity.ok(userService.editUserDetails(editUserRequest));
    }
}
