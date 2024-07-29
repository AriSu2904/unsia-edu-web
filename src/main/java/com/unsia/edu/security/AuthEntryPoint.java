package com.unsia.edu.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unsia.edu.models.common.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("Authentication failed: {}", authException.getMessage());
        log.error("Request URI: {}", request.getRequestURI());
        log.error("Authorization header: {}", request.getHeader("Authorization"));

        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .errors("AUTHENTICATION_FAILED " + authException.getMessage())
                .build();

        String commonResponseString = objectMapper.writeValueAsString(commonResponse);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(commonResponseString);
    }
}
