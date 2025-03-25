package com.tekerasoft.arzuamber.service;

import com.tekerasoft.arzuamber.dto.UserDto;
import com.tekerasoft.arzuamber.dto.request.CreateUserRequest;
import com.tekerasoft.arzuamber.dto.request.LoginRequest;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.dto.response.JwtResponse;
import com.tekerasoft.arzuamber.exception.UserException;
import com.tekerasoft.arzuamber.exception.UserRegisterException;
import com.tekerasoft.arzuamber.model.User;
import com.tekerasoft.arzuamber.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       AuthenticationManager authenticationManager, BCryptPasswordEncoder bCryptPasswordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
    }



    public JwtResponse authenticate(LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.email());
        if (user.isEmpty()) {
            throw new UserException("Email or password is incorrect");
        }
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
            );
            if (auth.isAuthenticated()) {
                return new JwtResponse(
                        jwtService.generateToken(addClaims(loginRequest.email()), loginRequest.email()),
                        jwtService.generateRefreshToken(addClaims(loginRequest.email()), loginRequest.email())
                );
            }
        } catch (Exception e) {
            throw new UserException("Email or password is incorrect");
        }
        throw new UserException("Email or password is incorrect");
    }

    public ApiResponse<?> register(CreateUserRequest req) {
        try {
            Optional<User> user = userRepository.findByEmail(req.getEmail());
            if(user.isPresent()) {
                throw new UserRegisterException("Email already in use");
            }else {
                userRepository.save(UserDto.createUserEntity(req,bCryptPasswordEncoder));
                return new ApiResponse<>("User created successfully",null,true);
            }
        } catch (RuntimeException e) {
            throw new UserRegisterException(e.getMessage());
        }
    }

    public JwtResponse refreshToken(String refreshToken) {
        try {
            // Token'ın geçerliliğini kontrol et
            if (jwtService.isTokenExpired(refreshToken)) {
                throw new UserException("Refresh token expired. Please log in again.");
            }

            // Token geçerli ise, içinden kullanıcı bilgilerini al
            String email = jwtService.extractUser(refreshToken);
            Optional<User> user = userRepository.findByEmail(email);

            if (user.isEmpty()) {
                throw new UserException("User not found");
            }

            // Yeni Access Token ve Refresh Token oluştur
            String newAccessToken = jwtService.generateToken(addClaims(email), email);
            String newRefreshToken = jwtService.generateRefreshToken(addClaims(email), email);

            return new JwtResponse(newAccessToken, newRefreshToken);
        } catch (Exception e) {
            throw new UserException("Invalid refresh token");
        }
    }

    private Map<String, Object> addClaims(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.get().getId());
        claims.put("role", user.get().getAuthorities());
        claims.put("email", user.get().getEmail());
        if(user.get().getPhoneNumber() != null) {
            claims.put("phoneNumber", user.get().getPhoneNumber());
        }
        if(user.get().getAddress() != null) {
            claims.put("address", user.get().getAddress());
        }
        claims.put("nameSurname", user.get().getFirstName()+ " " + user.get().getLastName());
        return claims;
    }
}
