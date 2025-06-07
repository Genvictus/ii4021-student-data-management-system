package com.std_data_mgmt.app.controllers;

import com.std_data_mgmt.app.dtos.LoginRequestDto;
import com.std_data_mgmt.app.dtos.RegisterRequestDto;
import com.std_data_mgmt.app.entities.User;
import com.std_data_mgmt.app.security.jwt.JwtKeyProvider;
import com.std_data_mgmt.app.services.AuthService;
import com.std_data_mgmt.app.utils.FormattedResponseEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PrivateKey;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtKeyProvider jwtKeyProvider;

    public AuthController(AuthService authService, JwtKeyProvider jwtKeyProvider) {
        this.authService = authService;
        this.jwtKeyProvider = jwtKeyProvider;
    }

    @PostMapping("/register")
    public FormattedResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDto request) {
        authService.register(request.toUser());
        return new FormattedResponseEntity<>(
                HttpStatus.CREATED,
                true,
                "User registered successfully",
                null
        );
    }

    @PostMapping("/login")
    public FormattedResponseEntity<Void> login(
            @Valid @RequestBody LoginRequestDto request,
            HttpServletResponse httpResponse
    ) {
        Optional<User> authenticatedUser = authService.authenticate(request.getEmail(), request.getPassword());

        if (authenticatedUser.isEmpty()) {
            return new FormattedResponseEntity<>(
                    HttpStatus.BAD_REQUEST,
                    false,
                    "Invalid credentials",
                    null
            );
        }

        User user = authenticatedUser.get();

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

        return new FormattedResponseEntity<>(HttpStatus.OK, true, "Login Successful", null);
    }
}