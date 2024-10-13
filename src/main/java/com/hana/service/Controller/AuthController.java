package com.hana.service.Controller;


import com.hana.service.Common.Const;
import com.hana.service.Common.FunctionPath;
import com.hana.service.DAO.Entity.UserEntity;
import com.hana.service.DAO.Repository.UserRepository;
import com.hana.service.Request.AuthRequest;
import com.hana.service.Response.AuthResponse;
import com.hana.service.Response.BaseResponse;
import com.hana.service.Response.ErrorResponse;
import com.hana.service.Utils.JwtTokenUtil;
import com.hana.service.Utils.LogUtils;
import com.hana.service.Utils.Methods;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;

@RestController
@Tag(name = "Auth")
public class AuthController {

    private static LogUtils logger = new LogUtils();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping(FunctionPath.AUTH.login)
    @Operation(summary = "login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AuthResponse.LoginResponse.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<? extends BaseResponse> login(@RequestBody @Valid AuthRequest.LoginRequest loginRequest, HttpServletRequest request) {
        UserEntity user = userRepository.findByAccount(loginRequest.getAccount()).orElse(null);;

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.getAccount(), null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(new ErrorResponse(request, "Invalid account or password"));
        }

        if (!user.getStatus().equals(Const.USER_STATUS_ACTIVE)) {
            return ResponseEntity.badRequest().body(new ErrorResponse(request, "User account is not active"));
        }

        Instant tokenExpiredTime = Instant.now().plus(5, ChronoUnit.HOURS);
        String token = jwtTokenUtil.getUserValidJWT(userRepository, jwtTokenUtil, user);

        user.setLoginToken(token);
        user.setTokenExpiredTime(LocalDateTime.ofInstant(tokenExpiredTime, ZoneId.of("UTC")));
        userRepository.save(user);

        AuthResponse.LoginResponse response = new AuthResponse.LoginResponse(request);
        response.setLoginToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(FunctionPath.AUTH.logout)
    @Operation(summary = "logout")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
    })
    public ResponseEntity<? extends BaseResponse> logout(HttpServletRequest request) {
        String userAccount = Methods.getUserAccountBySecurityContextHolder(SecurityContextHolder.getContext().getAuthentication());
        long userId = userRepository.findByAccount(userAccount).get().getId();
        Optional<UserEntity> optional = userRepository.findById(userId);
        if (optional.isPresent()){
            UserEntity userEntity = optional.get();
            userEntity.setLoginToken(null);
            userEntity.setTokenExpiredTime(null);
            userRepository.save(userEntity);
        }

        BaseResponse response = new BaseResponse(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
