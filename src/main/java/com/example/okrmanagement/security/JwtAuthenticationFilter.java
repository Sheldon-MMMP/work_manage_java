package com.example.okrmanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.okrmanagement.exception.BusinessException;
import com.example.okrmanagement.common.ErrorCode;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null) {
                // 直接调用getUuIdFromJwtToken，它会抛出JWT异常
                String uuId = jwtUtils.getUuIdFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(uuId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
            sendErrorResponse(response, ErrorCode.TOKEN_EXPIRED);
        } catch (io.jsonwebtoken.UnsupportedJwtException | io.jsonwebtoken.MalformedJwtException | io.jsonwebtoken.SignatureException | IllegalArgumentException e) {
            logger.error("JWT token is invalid: {}", e.getMessage());
            sendErrorResponse(response, ErrorCode.INVALID_TOKEN);
        } catch (BusinessException e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
            sendErrorResponse(response, e.getCode(), e.getMessage());
        }
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        sendErrorResponse(response, errorCode.getCode(), errorCode.getMessage());
    }

    private void sendErrorResponse(HttpServletResponse response, int code, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String json = String.format("{\"code\": %d, \"message\": \"%s\", \"data\": null}", code, message);
        response.getWriter().write(json);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}