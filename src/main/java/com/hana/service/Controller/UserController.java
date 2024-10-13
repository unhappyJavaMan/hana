package com.hana.service.Controller;

import com.hana.service.Common.Const;
import com.hana.service.Common.FunctionPath;
import com.hana.service.DAO.Entity.UserEntity;
import com.hana.service.DAO.Repository.UserRepository;
import com.hana.service.Request.UserRequest;
import com.hana.service.Response.BaseResponse;
import com.hana.service.Response.ErrorResponse;
import com.hana.service.Response.UserResponse;
import com.hana.service.Utils.LogUtils;
import com.hana.service.Utils.TimeUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "User")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private static LogUtils logger = new LogUtils();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(FunctionPath.user.create)
    @Operation(summary = "user create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Transactional
    public ResponseEntity<? extends BaseResponse> create(@RequestBody @Valid UserRequest.createUser user, HttpServletRequest request) {
        if (userRepository.existsByAccount(user.getAccount())) {
            return ResponseEntity.badRequest().body(new ErrorResponse(request, "userAccount already exists"));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new ErrorResponse(request, "email already exists"));
        }

        if (userRepository.existsByPhone(user.getPhone())) {
            return ResponseEntity.badRequest().body(new ErrorResponse(request, "phone already exists"));
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setNickName(user.getNickName());
        userEntity.setAccount(user.getAccount());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setEmail(user.getEmail());
        userEntity.setPhone(user.getPhone());
        userEntity.setStatus(Const.USER_STATUS_ACTIVE);
        userEntity.setCreateDate(TimeUtils.getNowUTCLocalDateTime());
        userRepository.save(userEntity);

        String message = "add user " + user.getAccount();
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(request, message));
    }

    // 删除用户
    @PostMapping(FunctionPath.user.delete)
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<? extends BaseResponse> deleteUser(@RequestBody @Valid UserRequest.Delete deleteRequest, HttpServletRequest request) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(deleteRequest.getId());
        if (!userEntityOptional.isPresent()){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(request, "User not found"));
        }
        UserEntity userEntity = userEntityOptional.get();
        userRepository.delete(userEntity);

        String message = "delete user " + userRepository.findById(deleteRequest.getId()).get().getAccount();
        return ResponseEntity.ok(new BaseResponse(request, message));
    }

    // 修改用户信息
    @PostMapping(FunctionPath.user.update)
    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Transactional
    public ResponseEntity<? extends BaseResponse> updateUser(@RequestBody @Valid UserRequest.Update user, HttpServletRequest request) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(user.getId());
        if (userEntityOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(request, "User not found"));
        }
        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setNickName(user.getNickName());
        newUserEntity.setEmail(user.getEmail());
        newUserEntity.setPhone(user.getPhone());
        newUserEntity.setUpdateDate(TimeUtils.getNowUTCLocalDateTime());
        userRepository.save(newUserEntity);


        String message = " update user " + user.toString();
        return ResponseEntity.ok(new BaseResponse(request, message));
    }

    // 查询用户
    @PostMapping(FunctionPath.user.getUserById)
    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.GetById.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getUserById(@RequestBody @Valid UserRequest.Get getRequest, HttpServletRequest request) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(getRequest.getId());
        if (!userEntityOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(request, "User not found"));
        }

        // setUser
        UserResponse.GetById response = new UserResponse.GetById(request);
        response.setUser(UserResponse.UserDTO.fromEntity(userEntityOptional.get()));

        String message = " get user info :" + userRepository.findById(getRequest.getId()).get().getAccount();
        response.setMessage(message);
        return ResponseEntity.ok(response);
    }

    // 查询全部用户
    @PostMapping(FunctionPath.user.getAll)
    @Operation(summary = "Get All user ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.GetAll.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        // 获取用户分页数据
        List<UserResponse.UserDTO> userDTOs = new ArrayList<>();
        List<UserEntity> userList = userRepository.findAll();
        for (UserEntity userEntity : userList) {
            UserResponse.UserDTO dto = UserResponse.UserDTO.fromEntity(userEntity);
            userDTOs.add(dto);
        }

        String message = " get all user info";
        UserResponse.GetAll response = new UserResponse.GetAll(request);
        response.setUsers(userDTOs);
        response.setMessage(message);
        return ResponseEntity.ok(response);
    }


    // 更改密码
    @PostMapping(FunctionPath.user.changePassword)
    @Operation(summary = "Change user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Transactional
    public ResponseEntity<? extends BaseResponse> changePassword(@RequestBody @Valid UserRequest.ChangePassword changePasswordRequest, HttpServletRequest request) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(changePasswordRequest.getId());
        if (!userEntityOptional.isPresent()){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(request, "User not found"));
        }
        UserEntity user = userEntityOptional.get();
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(new ErrorResponse(request, "Current password is incorrect"));
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        user.setUpdateDate(TimeUtils.getNowUTCLocalDateTime());
        userRepository.save(user);

        return ResponseEntity.ok(new BaseResponse(request));
    }

}
