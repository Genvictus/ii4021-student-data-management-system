package com.std_data_mgmt.app.controllers;

import com.std_data_mgmt.app.dtos.LoginRequestDto;
import com.std_data_mgmt.app.dtos.RegisterRequestDto;
import com.std_data_mgmt.app.dtos.ResponseDto;
import com.std_data_mgmt.app.entities.User;
import com.std_data_mgmt.app.enums.Role;
import com.std_data_mgmt.app.security.jwt.JwtKeyProvider;
import com.std_data_mgmt.app.services.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseDto<Void>> register(@RequestBody RegisterRequestDto request) {
        try {
            authService.register(
                    request.getUserId(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getFullName(),
                    Role.valueOf(request.getRole().toUpperCase()),
                    request.getPublicKey(),
                    request.getDepartmentId(),
                    Optional.of(request.getSupervisorId())
            );
            ResponseDto<Void> response = new ResponseDto<>(true, "User registered successfully", null);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            ResponseDto<Void> response = new ResponseDto<>(false, "Invalid parameter", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ResponseDto<Void> response = new ResponseDto<>(false, "Internal server error", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Void>> login(
            @RequestBody LoginRequestDto request,
            HttpServletResponse httpResponse
    ) {

        Optional<User> authenticatedUser = authService.authenticate(request.getEmail(), request.getPassword());

        if (authenticatedUser.isEmpty()) {
            ResponseDto<Void> response = new ResponseDto<Void>(false, "Invalid credentials", null);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
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

        ResponseDto<Void> response = new ResponseDto<Void>(true, "Login Successful", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}