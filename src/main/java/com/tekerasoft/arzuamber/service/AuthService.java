package com.tekerasoft.arzuamber.service;

import com.tekerasoft.arzuamber.dto.UserDto;
import com.tekerasoft.arzuamber.dto.request.CreateUserRequest;
import com.tekerasoft.arzuamber.dto.request.LoginRequest;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.dto.response.JwtResponse;
import com.tekerasoft.arzuamber.exception.UserException;
import com.tekerasoft.arzuamber.model.User;
import com.tekerasoft.arzuamber.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public JwtResponse authenticate(LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.email());
        try {
            if(user.isPresent()) {
                Authentication auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.email(),loginRequest.password())
                );
                if(auth.isAuthenticated()) {
                    return new JwtResponse(
                            jwtService.generateToken(addClaims(loginRequest.email()),loginRequest.email())
                    );
                }else {
                    throw new UserException("Email or password is incorrect");
                }
            }
        } catch (RuntimeException e) {
            throw new UserException("Email or password is incorrect");
        }
        throw new UserException("Email or password is incorrect");
    }

    public ApiResponse<?> register(CreateUserRequest req) {
        try {
            Optional<User> user = userRepository.findByEmail(req.getEmail());
            if(user.isPresent()) {
                throw new UserException("Email already in use");
            }else {
                userRepository.save(UserDto.createUserEntity(req,passwordEncoder));
                return new ApiResponse<>("User created successfully",null,true);
            }
        } catch (RuntimeException e) {
            throw new UserException(e.getMessage());
        }
    }

    private Map<String, Object> addClaims(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.get().getId());
        claims.put("role", user.get().getAuthorities());
        claims.put("email", user.get().getEmail());
        claims.put("nameSurname", user.get().getFirstName()+ " " + user.get().getLastName());
        return claims;
    }
}
