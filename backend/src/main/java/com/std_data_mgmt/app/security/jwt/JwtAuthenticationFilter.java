package com.std_data_mgmt.app.security.jwt;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtAuthenticationFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    @Autowired
    private JwtKeyProvider jwtKeyProvider;


    @Override
    public void doFilter(
        ServletRequest request, 
        ServletResponse response, 
        FilterChain chain
    ) throws IOException, ServletException {
        logger.info("JwtAuthenticationFilter::doFilter is called");


        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = null;
        Cookie[] cookies = httpRequest.getCookies();

        if (cookies != null) {
            for (Cookie cookie:  cookies) {
                if (cookie.getName().equals("access-token")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            try {
                Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtKeyProvider.getPublicKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

                String userId = claims.getSubject();
                String role = claims.get("role", String.class);

                logger.info("Authenticated userId: {}, role: {}", userId, role);

                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId, token, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (ExpiredJwtException e) {
                logger.error(e.toString());
                // handle this later
            } catch (JwtException e) {
                logger.error(e.toString());
                // handle this later
            }
        }

        chain.doFilter(request, response);
    }
}
