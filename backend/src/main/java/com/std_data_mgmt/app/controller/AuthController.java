package com.std_data_mgmt.app.controller;

import java.security.PrivateKey;
import java.util.Date;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.std_data_mgmt.app.dto.LoginRequest;
import com.std_data_mgmt.app.dto.LoginResponse;
import com.std_data_mgmt.app.dto.RegisterRequest;
import com.std_data_mgmt.app.entity.User;
import com.std_data_mgmt.app.entity.UserRole;
import com.std_data_mgmt.app.security.JwtKeyProvider;
import com.std_data_mgmt.app.service.AuthService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtKeyProvider jwtKeyProvider;

    public AuthController(AuthService authService, JwtKeyProvider jwtKeyProvider) {
        this.authService = authService;
        this.jwtKeyProvider = jwtKeyProvider;
    }

    @GetMapping
    public String check() {
        return "OK";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            authService.register(
                request.getUserId(),
                request.getEmail(),
                request.getPassword(),
                request.getFullName(),
                UserRole.valueOf(request.getRole().toUpperCase()),
                request.getPublicKey()
            );
            return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Registration failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse httpResponse) {

        Optional<User> authenticatedUser = authService.authenticate(request.getEmail(), request.getPassword());

        if (authenticatedUser.isPresent()) {
            User user = authenticatedUser.get();
            LoginResponse response = new LoginResponse(
                user.getUserId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().name(),
                "Login successful!"
            );

            PrivateKey jwtSigningKey = this.jwtKeyProvider.getPrivateKey();

            String jwtToken = Jwts.builder()
                .setSubject(String.valueOf(user.getUserId()))
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtKeyProvider.getExpirationMs()))
                .signWith(jwtSigningKey, SignatureAlgorithm.RS256)
                .compact();

            Cookie jwtCookie = new Cookie("access-token", jwtToken);
            jwtCookie.setAttribute("SameSite", "strict");
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge((int) (jwtKeyProvider.getExpirationMs() / 1000));

            httpResponse.addCookie(jwtCookie);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            LoginResponse errorResponse = new LoginResponse(
                null, null, null, null, "Invalid credentials."
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }
}