package com.example.demo.controller;

import com.hana.service.Common.Const;
import com.hana.service.Controller.AuthController;
import com.hana.service.DAO.Entity.UserEntity;
import com.hana.service.DAO.Repository.UserRepository;
import com.hana.service.Request.AuthRequest;
import com.hana.service.Response.AuthResponse;
import com.hana.service.Response.BaseResponse;
import com.hana.service.Response.ErrorResponse;
import com.hana.service.Utils.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(httpServletRequest.getAttribute(Const.REQUEST_ID)).thenReturn(UUID.randomUUID().toString());
    }

    @Test
    void testLogin_Success() {
        AuthRequest.LoginRequest loginRequest = new AuthRequest.LoginRequest();
        loginRequest.setAccount("testuser");
        loginRequest.setPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setAccount("testuser");
        userEntity.setPassword("encodedPassword");
        userEntity.setStatus(Const.USER_STATUS_ACTIVE);

        when(userRepository.findByAccount("testuser")).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtTokenUtil.getUserValidJWT(eq(userRepository), eq(jwtTokenUtil), any(UserEntity.class))).thenReturn("jwtToken");

        ResponseEntity<? extends BaseResponse> response = authController.login(loginRequest, httpServletRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof AuthResponse.LoginResponse);
        assertEquals("jwtToken", ((AuthResponse.LoginResponse) response.getBody()).getLoginToken());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testLogin_InvalidCredentials() {
        AuthRequest.LoginRequest loginRequest = new AuthRequest.LoginRequest();
        loginRequest.setAccount("testuser");
        loginRequest.setPassword("wrongpassword");

        when(userRepository.findByAccount("testuser")).thenReturn(Optional.empty());

        ResponseEntity<? extends BaseResponse> response = authController.login(loginRequest, httpServletRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        assertEquals("Invalid account or password", ((ErrorResponse) response.getBody()).getMessage());
    }

    @Test
    void testLogin_InactiveUser() {
        AuthRequest.LoginRequest loginRequest = new AuthRequest.LoginRequest();
        loginRequest.setAccount("testuser");
        loginRequest.setPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setAccount("testuser");
        userEntity.setPassword("encodedPassword");
        userEntity.setStatus("INACTIVE");

        when(userRepository.findByAccount("testuser")).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        ResponseEntity<? extends BaseResponse> response = authController.login(loginRequest, httpServletRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        assertEquals("User account is not active", ((ErrorResponse) response.getBody()).getMessage());
    }

    @Test
    void testLogout_Success() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setAccount("testuser");

        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken("testuser", null));
        when(userRepository.findByAccount("testuser")).thenReturn(Optional.of(userEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        ResponseEntity<? extends BaseResponse> response = authController.logout(httpServletRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof BaseResponse);
        verify(userRepository, times(1)).save(any(UserEntity.class));
        assertNull(userEntity.getLoginToken());
        assertNull(userEntity.getTokenExpiredTime());
    }
}
