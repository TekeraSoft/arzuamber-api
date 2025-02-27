package com.tekerasoft.arzuamber.controller;

import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.model.Contact;
import com.tekerasoft.arzuamber.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/contact")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createContact(@RequestBody Contact contact) {
        return ResponseEntity.ok(contactService.createContact(contact));
    }
}
