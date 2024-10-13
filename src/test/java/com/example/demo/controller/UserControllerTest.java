package com.example.demo.controller;

import com.hana.service.Common.Const;
import com.hana.service.Controller.UserController;
import com.hana.service.DAO.Entity.UserEntity;
import com.hana.service.DAO.Repository.UserRepository;
import com.hana.service.Request.UserRequest;
import com.hana.service.Response.BaseResponse;
import com.hana.service.Response.ErrorResponse;
import com.hana.service.Response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(httpServletRequest.getAttribute(Const.REQUEST_ID)).thenReturn(UUID.randomUUID().toString());
    }

    @Test
    void testCreateUser_Success() {
        UserRequest.createUser createUserRequest = new UserRequest.createUser();
        createUserRequest.setAccount("testuser");
        createUserRequest.setEmail("test@example.com");
        createUserRequest.setPhone("1234567890");
        createUserRequest.setPassword("password");

        when(userRepository.existsByAccount(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByPhone(anyString())).thenReturn(false);

        ResponseEntity<? extends BaseResponse> response = userController.create(createUserRequest, httpServletRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof BaseResponse);
    }

    @Test
    void testDeleteUser_Success() {
        UserRequest.Delete deleteRequest = new UserRequest.Delete();
        deleteRequest.setId(1L);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setAccount("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        ResponseEntity<? extends BaseResponse> response = userController.deleteUser(deleteRequest, httpServletRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof BaseResponse);
        verify(userRepository, times(1)).delete(userEntity);
    }

    @Test
    void testGetUserById_Success() {
        UserRequest.Get getRequest = new UserRequest.Get();
        getRequest.setId(1L);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setAccount("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        ResponseEntity<?> response = userController.getUserById(getRequest, httpServletRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof UserResponse.GetById);
    }

    @Test
    void testGetAll_Success() {
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setAccount("user1");

        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setAccount("user2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        ResponseEntity<?> response = userController.getAll(httpServletRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof UserResponse.GetAll);
        UserResponse.GetAll getAllResponse = (UserResponse.GetAll) response.getBody();
        assertEquals(2, getAllResponse.getUsers().size());
    }

    @Test
    void testChangePassword_Success() {
        UserRequest.ChangePassword changePasswordRequest = new UserRequest.ChangePassword();
        changePasswordRequest.setId(1L);
        changePasswordRequest.setCurrentPassword("oldPassword");
        changePasswordRequest.setNewPassword("newPassword");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setPassword("encodedOldPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        ResponseEntity<? extends BaseResponse> response = userController.changePassword(changePasswordRequest, httpServletRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof BaseResponse);
        verify(userRepository, times(1)).save(userEntity);
        assertEquals("encodedNewPassword", userEntity.getPassword());
    }
}