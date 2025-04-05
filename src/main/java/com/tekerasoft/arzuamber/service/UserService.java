package com.tekerasoft.arzuamber.service;

import com.tekerasoft.arzuamber.dto.ProductDto;
import com.tekerasoft.arzuamber.dto.UserAdminDto;
import com.tekerasoft.arzuamber.dto.request.EditUserRequest;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.model.Role;
import com.tekerasoft.arzuamber.model.User;
import com.tekerasoft.arzuamber.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PagedResourcesAssembler<UserAdminDto> pagedResourcesAssembler;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, PagedResourcesAssembler<UserAdminDto> pagedResourcesAssembler) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByEmail(username);
        return user.orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    public String createToken(String email) {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                return jwtService.generateToken(emptyMap(),email);
            }else {
                throw new UsernameNotFoundException(email);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ApiResponse<?> forgotPassword(String email, String password, String token) {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                boolean isTokenValid = jwtService.validateToken(token,user.get());
                if(!isTokenValid) {
                    throw new RuntimeException("Doğrulama başarılı değil yetkisiz işlem");
                }
                user.get().setHashedPassword(passwordEncoder.encode(password));
                userRepository.save(user.get());
                return new ApiResponse<>("Şifre başarıyla değiştirildi giriş yapabilirsiniz",null,true);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
        return new ApiResponse<>("Beklenmedik bir hata oluştu lütfen tekrar deneyin",null,false);
    }


    public ApiResponse<?> changePassword(String email,String oldPassword,String password, String token) {
        try {
            Optional<User> useroptional = userRepository.findByEmail(email);
            if(useroptional.isPresent()) {
                User user = useroptional.get();
                boolean isTokenValid = jwtService.validateToken(token,user);
                if(!isTokenValid) {
                    throw new RuntimeException("Doğrulama başarılı değil yetkisiz işlem");
                }
                if(!passwordEncoder.matches(oldPassword,user.getHashedPassword())) {
                    throw new RuntimeException("Eski parolanız hatalı lütfen tekrar deneyin.");
                }
                user.setHashedPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                return new ApiResponse<>("Parolanız başarıyla değiştirildi",null,true);
            } else {
                throw new RuntimeException("Kullanıcı bulunamadı!");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("İşlem başarısız sunucu cevap vermedi. Lütfen tekrar deneyiniz.");
        }

    }

    public boolean checkToken(String token, User user) {
        return jwtService.validateToken(token,user);
    }

    public ApiResponse<?> editUserDetails(EditUserRequest editUserRequest) {
        Optional<User> useroptional = userRepository.findByEmail(editUserRequest.getEmail());
        try {
            if(useroptional.isPresent()) {
                User user = useroptional.get();
                user.setAddress(editUserRequest.getAddress());
                user.setPhoneNumber(editUserRequest.getPhoneNumber());
                userRepository.save(user);
                return new ApiResponse<>("Bilgileriniz Güncellendi",null,true);
            } else {
                throw new RuntimeException("Güncelleme başarısız sunucu cevap vermedi.");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public PagedModel<EntityModel<UserAdminDto>> getUsers(int page, int size) {
        return pagedResourcesAssembler.toModel(userRepository.
                findAll(PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "createdAt")))
                .map(UserAdminDto::toDto));
    }

    public ApiResponse<?> changeUserRole(String id, Role role) {
        try {
            User user = userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
            Objects.requireNonNull(user.getRoles()).clear();
            user.getRoles().add(role);
            userRepository.save(user);
            return new ApiResponse<>("Role Changed",null,true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
