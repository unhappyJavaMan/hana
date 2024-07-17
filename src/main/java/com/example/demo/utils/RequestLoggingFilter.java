package com.example.demo.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    private String getHeadersInfo(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        for (String headerName : Collections.list(request.getHeaderNames())) {
            headers.append(headerName).append(": ").append(request.getHeader(headerName)).append("\n");
        }
        return headers.toString();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 記錄請求信息
        logger.info("Request URL: {}", request.getRequestURL());
        logger.info("Request Method: {}", request.getMethod());
        logger.info("Request Headers: {}", getHeadersInfo(request));
        logger.info("Request Parameters: {}", request.getParameterMap().toString());

        // 繼續過濾鏈
        filterChain.doFilter(request, response);
    }
}