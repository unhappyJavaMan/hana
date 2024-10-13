package com.hana.service.Controller;

import com.hana.service.Common.Const;
import com.hana.service.Common.FunctionPath;
import com.hana.service.DAO.Entity.CustomerEntity;
import com.hana.service.DAO.Repository.CustomerRepository;
import com.hana.service.Request.CustomerRequest;
import com.hana.service.Response.BaseResponse;
import com.hana.service.Response.CustomerResponse;
import com.hana.service.Response.ErrorResponse;
import com.hana.service.Utils.TimeUtils;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Customer")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping(FunctionPath.customer.create)
    @Operation(summary = "Create customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<? extends BaseResponse> createCustomer(@RequestBody @Valid CustomerRequest.Create request, HttpServletRequest httpRequest) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new ErrorResponse(httpRequest, "Email already exists"));
        }

        CustomerEntity customer = new CustomerEntity();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setStatus(Const.USER_STATUS_ACTIVE);
        customer.setCreateDate(TimeUtils.getNowUTCLocalDateTime());

        customerRepository.save(customer);

        return ResponseEntity.ok(new BaseResponse(httpRequest, "Customer created successfully"));
    }

    @GetMapping(FunctionPath.customer.getAll)
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

    @GetMapping(FunctionPath.customer.getById)
    @Operation(summary = "Get customer by ID")
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

    // 其他方法如更新、刪除等可以根據需要添加
}