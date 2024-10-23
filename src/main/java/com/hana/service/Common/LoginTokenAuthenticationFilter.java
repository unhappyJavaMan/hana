package com.hana.service.Common;

import com.hana.service.DAO.Entity.UserEntity;
import com.hana.service.DAO.Repository.UserRepository;
import com.hana.service.Utils.JwtTokenUtil;
import com.hana.service.Utils.LogUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class LoginTokenAuthenticationFilter extends OncePerRequestFilter {

    private static LogUtils logger = new LogUtils();
    private UserRepository userRepository;

    @Autowired
    public LoginTokenAuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorHeader = request.getHeader(AUTHORIZATION);
        String bearer = "Bearer ";
        System.out.println(authorHeader);

        if (authorHeader != null && authorHeader.startsWith(bearer)) {
            String token = authorHeader.substring(bearer.length());

            try {
                Claims claims = JwtTokenUtil.getAllClaimsFromToken(token);
                logger.info("JWT payload:" + claims.toString());
                String tokenUserName = JwtTokenUtil.getUsernameFromToken(token);

                Optional<UserEntity> userEntity = userRepository.findByAccountAndLoginToken(tokenUserName, token);
                if (!userEntity.isPresent()){
                    logger.info("token not exist in db");
                    response.setStatus(UNAUTHORIZED.value());
                    return;
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userEntity.get().getAccount(), null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            }  catch (Exception e) {
                logger.error(e);
                response.setStatus(UNAUTHORIZED.value());
            }
        }else {
            logger.info("Header error:" + authorHeader);
            response.setStatus(UNAUTHORIZED.value());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return isSwggerPath(path)||  isNotNeedAuthorizationPath(path);
    }

    private static boolean isSwggerPath(String path) {
        return Arrays.stream(Const.METHODS_NOT_NEED_AUTHORIZATION_SWAAGER).anyMatch(path::startsWith);
    }


    private static boolean isNotNeedAuthorizationPath(String path) {
        return Arrays.stream(Const.METHODS_NOT_NEED_AUTHORIZATION).anyMatch(path::equals);
    }

}
