package com.hana.service.Controller;

import com.hana.service.Common.FunctionPath;
import com.hana.service.DAO.Entity.UserServiceEntity;
import com.hana.service.DAO.Repository.UserRepository;
import com.hana.service.Request.UserServiceRequest;
import com.hana.service.Response.AuthResponse;
import com.hana.service.Response.BaseResponse;
import com.hana.service.Response.ErrorResponse;
import com.hana.service.Response.UserServiceResponse;
import com.hana.service.Service.UserServiceService;
import com.hana.service.Utils.Methods;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Service")
public class UserServiceController {
    @Autowired
    private UserServiceService userServiceService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(FunctionPath.userService.getServicesByUserId)
    @Operation(summary = "get Services By UserId",
            description = FunctionPath.userService.getServicesByUserId)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserServiceResponse.GetByUserId.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getServicesByUserId(HttpServletRequest request) {
        String userAccount = Methods.getUserAccountBySecurityContextHolder(SecurityContextHolder.getContext().getAuthentication());
        Long userId = userRepository.findByAccount(userAccount).get().getId();

        List<UserServiceEntity> services = userServiceService.getServicesByUserId(userId);
        List<UserServiceResponse.UserServiceDTO> dtos = services.stream()
                .map(UserServiceResponse.UserServiceDTO::fromEntity)
                .collect(Collectors.toList());

        UserServiceResponse.GetByUserId response = new UserServiceResponse.GetByUserId(request);
        response.setServices(dtos);
        return ResponseEntity.ok(response);
    }

    @PostMapping(FunctionPath.userService.createServiceByUser)
    @Operation(summary = "create Service By User",
            description = FunctionPath.userService.createServiceByUser)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> createServiceByUser(
            @RequestBody @Valid UserServiceRequest.Create request,
            HttpServletRequest httpRequest) {
        String userAccount = Methods.getUserAccountBySecurityContextHolder(SecurityContextHolder.getContext().getAuthentication());
        Long userId = userRepository.findByAccount(userAccount).get().getId();

        userServiceService.createService(userId, request);
        return ResponseEntity.ok(new BaseResponse(httpRequest, "Create service successfully"));
    }

    // 更新和刪除的端點類似...
}
