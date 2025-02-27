package com.tekerasoft.arzuamber.service;

import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.model.Contact;
import com.tekerasoft.arzuamber.repository.ContactRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final PagedResourcesAssembler<Contact> pagedResourcesAssembler;

    public ContactService(ContactRepository contactRepository, PagedResourcesAssembler<Contact> pagedResourcesAssembler) {
        this.contactRepository = contactRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    public ApiResponse<?> createContact(Contact contact) {
        try {
            contactRepository.save(contact);
            return new ApiResponse<>("Bilgler kaydedildi en kısa sürede iletişime geçeceğiz.",null,true);
        } catch (RuntimeException e) {
            throw new RuntimeException("Beklenmedik bir hata oluştu !");
        }
    }

    public PagedModel<EntityModel<Contact>> getAllContacts(int page, int size) {
        return pagedResourcesAssembler.toModel(contactRepository.findAll(PageRequest.of(page,size)));
    }

    public ApiResponse<?> deleteContact(String id) {
        try {
            contactRepository.deleteById(UUID.fromString(id));
            return new ApiResponse<>("Contact details deleted", null,true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
