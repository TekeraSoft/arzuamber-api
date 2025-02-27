package com.tekerasoft.arzuamber.utils;

import com.tekerasoft.arzuamber.service.AuthService;
import com.tekerasoft.arzuamber.service.JwtService;
import com.tekerasoft.arzuamber.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Token doğrulamasını atlamak istediğimiz endpoint'ler
        String path = request.getRequestURI();
        if (path.startsWith("/v1/api/product") ||
                path.startsWith("/v1/api/order") ||
                path.startsWith("/v1/api/auth") ||
                path.startsWith("/v1/api/blog/**") ||
                path.startsWith("/v1/api/contact/**")) {
            filterChain.doFilter(request, response); // Doğrudan devam et
            return;
        }

        // Token doğrulama işlemi
        String username = null;
        try {
            String token = extractTokenFromCookies(request);
            if (token != null) {
                username = jwtService.extractUser(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userService.loadUserByUsername(username);
                    if (jwtService.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
            filterChain.doFilter(request, response); // Devam et
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expired");
            response.getWriter().flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(e.getMessage());
            response.getWriter().flush();
        }
    }

    public static String extractTokenFromCookies(HttpServletRequest request) {
        // Tüm cookie'leri al
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Eğer cookie'nin adı 'session-token' veya başka bir token adı ise
                if ("next-auth.session-token".equals(cookie.getName()) || "__Secure-next-auth.session-token".equals(cookie.getName())) {
                    return cookie.getValue(); // Token'ı döndür
                }
            }
        }
        return null; // Eğer cookie yoksa null döndür
    }
}

