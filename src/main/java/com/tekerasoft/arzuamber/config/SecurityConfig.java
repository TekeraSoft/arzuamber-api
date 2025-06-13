package com.tekerasoft.arzuamber.config;

import com.tekerasoft.arzuamber.model.Role;
import com.tekerasoft.arzuamber.service.UserService;
import com.tekerasoft.arzuamber.utils.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${spring.origin.url}")
    private String originUrl;

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(PasswordEncoder passwordEncoder, UserService userService,
                          JwtAuthFilter jwtAuthFilter) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize // WebSocket izin ver
                        .requestMatchers("/v1/api/product/**", "/v1/api/category/**",
                                "/ws/**","/app/**",
                                "/v1/api/order/**", "/v1/api/auth/**","/v1/api/user/**",
                                "/v1/api/blog/**", "/v1/api/contact/**","/v1/api/comment/**","/v1/api/admin/**",
                                "/v1/api/slider/**").permitAll()
                        .requestMatchers("/v1/api/admin/**").hasAnyAuthority(Role.ADMIN.name(),Role.SUPER_ADMIN.name())
                        .requestMatchers("/v1/api/super-admin/**").hasAnyAuthority(Role.SUPER_ADMIN.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Sadece güvenilir frontend origin'lerini burada tanımla
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",             // local geliştirme
                "http://192.168.1.20:3000",
                "http://78.135.83.4:3000", // local ağdaki test
                "https://frontend.arzuamber.com"     // gerçek frontend domain (deploy sonrası)
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*")); // İzin verilen header'lar
        configuration.setAllowCredentials(true); // Token, cookie gibi kimlik doğrulama verilerini taşıyabilmek için

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Tüm endpointler için uygula

        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return  authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

