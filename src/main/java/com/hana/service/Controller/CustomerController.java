package com.hana.service.Controller;

import com.hana.service.Common.Const;
import com.hana.service.Common.FunctionPath;
import com.hana.service.DAO.Entity.CustomerEntity;
import com.hana.service.DAO.Entity.UserCustomerMappingEntity;
import com.hana.service.DAO.Entity.UserEntity;
import com.hana.service.DAO.Repository.CustomerRepository;
import com.hana.service.DAO.Repository.UserCustomerMappingRepository;
import com.hana.service.DAO.Repository.UserRepository;
import com.hana.service.Request.CustomerRequest;
import com.hana.service.Response.BaseResponse;
import com.hana.service.Response.CustomerResponse;
import com.hana.service.Response.ErrorResponse;
import com.hana.service.Service.UserCustomerService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Customer")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCustomerMappingRepository userCustomerMappingRepository;
    @Autowired
    private UserCustomerService userCustomerService;
    @Autowired
    private UserCustomerMappingRepository mappingRepository;

    @PostMapping(FunctionPath.customer.getAll)
    @Operation(summary = "Get all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CustomerResponse.GetAll.class)))
    })
    public ResponseEntity<CustomerResponse.GetAll> getAllCustomers(HttpServletRequest httpRequest) {
        List<CustomerEntity> customers = customerRepository.findAll();
        List<CustomerResponse.CustomerDTO> customerDTOs = customers.stream()
                .map(CustomerResponse.CustomerDTO::fromEntity)
                .collect(Collectors.toList());

        CustomerResponse.GetAll response = new CustomerResponse.GetAll(httpRequest);
        response.setCustomers(customerDTOs);
        return ResponseEntity.ok(response);
    }

    @PostMapping(FunctionPath.customer.getById)
    @Operation(summary = "Get customer by ID",
            description = FunctionPath.customer.getById)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CustomerResponse.GetById.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getCustomerById(@PathVariable Long id, HttpServletRequest httpRequest) {
        Optional<CustomerEntity> customerOpt = customerRepository.findById(id);
        if (customerOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(httpRequest, "Customer not found"));
        }

        CustomerResponse.GetById response = new CustomerResponse.GetById(httpRequest);
        response.setCustomer(CustomerResponse.CustomerDTO.fromEntity(customerOpt.get()));
        return ResponseEntity.ok(response);
    }

    @PostMapping(FunctionPath.customer.getCustomerByUserId)
    @Operation(summary = "get Customer By User Id",
            description = FunctionPath.customer.getCustomerByUserId)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CustomerResponse.GetCustomerByUserId.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getCustomerByUserId(HttpServletRequest httpRequest) {
        String userAccount = Methods.getUserAccountBySecurityContextHolder(SecurityContextHolder.getContext().getAuthentication());
        long userId = userRepository.findByAccount(userAccount).get().getId();
        List<UserCustomerMappingEntity> mappingEntities = mappingRepository.findByUserId(userId);

        List<CustomerResponse.CustomerDTO> responseDatas = new ArrayList<>();
        for (UserCustomerMappingEntity mappingEntity : mappingEntities){
            CustomerEntity entityCustomer = mappingEntity.getCustomer();
            responseDatas.add(CustomerResponse.CustomerDTO.fromEntityAndMappingStatus(entityCustomer, mappingEntity));
        }

        CustomerResponse.GetCustomerByUserId response = new CustomerResponse.GetCustomerByUserId(httpRequest);
        response.setCustomers(responseDatas);
        return ResponseEntity.ok(response);
    }

    @PostMapping(FunctionPath.customer.createCustomerByUserId)
    @Operation(summary = "Create customer",
            description = FunctionPath.customer.createCustomerByUserId)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Transactional
    public ResponseEntity<? extends BaseResponse> createCustomerByUserId(@RequestBody @Valid CustomerRequest.Create request, HttpServletRequest httpRequest) {
        String userAccount = Methods.getUserAccountBySecurityContextHolder(SecurityContextHolder.getContext().getAuthentication());
        UserEntity user = userRepository.findByAccount(userAccount)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String gender = request.getGender();
        if (gender != null && !gender.trim().isEmpty()) {
            if (!Const.GENDER_TYPE_MALE.equals(gender) && !Const.GENDER_TYPE_FEMALE.equals(gender)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(httpRequest, "gender type error"));
            }
        }

        CustomerEntity customer = CustomerEntity.fromCreateByUserId(request);
        customer = customerRepository.save(customer);

        // 創建mapping關係
        UserCustomerMappingEntity mapping = new UserCustomerMappingEntity();
        mapping.setUser(user);
        mapping.setCustomer(customer);
        mapping.setCreateDate(LocalDateTime.now());
        mapping.setUpdateDate(LocalDateTime.now());
        mapping.setStatus(Const.USER_STATUS_ACTIVE);

        // 儲存mapping
        userCustomerMappingRepository.save(mapping);

        return ResponseEntity.ok(new BaseResponse(httpRequest, "Customer created successfully"));
    }

    @PostMapping(FunctionPath.customer.updateCustomerStatusByUser)
    @Operation(summary = "delete Customer By User",
            description = FunctionPath.customer.updateCustomerStatusByUser)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Transactional
    public ResponseEntity<? extends BaseResponse> updateCustomerStatusByUser(@RequestBody @Valid CustomerRequest.UpdateCustomerStatusByUser request, HttpServletRequest httpRequest) {
        String userAccount = Methods.getUserAccountBySecurityContextHolder(SecurityContextHolder.getContext().getAuthentication());
        Long customerId = request.getId();

        Optional<CustomerEntity> customerEntity = customerRepository.findById(customerId);
        if (customerEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(httpRequest, "this customerId not exist"));
        }

        // 檢查該客戶是否屬於當前用戶
        Optional<UserEntity> userEntity = userRepository.findByAccount(userAccount);
        if (userEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(httpRequest, "user not found"));
        }
        Long userId = userEntity.get().getId();

        Optional<UserCustomerMappingEntity> mappingEntity = mappingRepository.findByUserIdAndCustomerId(userId, customerEntity.get().getId());

        if (mappingEntity.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(httpRequest, "you don't have permission to disable this customer"));
        }
        String entityStatus = null;
        if (request.isStatus()){
            entityStatus = Const.USER_STATUS_ACTIVE;
        }else {
            entityStatus = Const.USER_STATUS_REVOKE;
        }

        UserCustomerMappingEntity entity = mappingEntity.get();
        entity.setStatus(entityStatus);
        mappingRepository.save(entity);

        return ResponseEntity.ok(new BaseResponse(httpRequest, "Revoke Customer " + customerEntity.get().getName() +" successfully"));
    }


    // 其他方法如更新、刪除等可以根據需要添加
}