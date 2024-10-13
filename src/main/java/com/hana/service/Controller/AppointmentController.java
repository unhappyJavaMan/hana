package com.hana.service.Controller;

import com.hana.service.Common.Const;
import com.hana.service.Common.FunctionPath;
import com.hana.service.DAO.Entity.AppointmentEntity;
import com.hana.service.DAO.Entity.CustomerEntity;
import com.hana.service.DAO.Entity.UserEntity;
import com.hana.service.DAO.Repository.AppointmentRepository;
import com.hana.service.DAO.Repository.CustomerRepository;
import com.hana.service.DAO.Repository.UserRepository;
import com.hana.service.Request.AppointmentRequest;
import com.hana.service.Response.AppointmentResponse;
import com.hana.service.Response.BaseResponse;
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
@Tag(name = "Appointment")
@SecurityRequirement(name = "bearerAuth")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(FunctionPath.appointment.create)
    @Operation(summary = "Create appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<? extends BaseResponse> createAppointment(@RequestBody @Valid AppointmentRequest.Create request, HttpServletRequest httpRequest) {
        Optional<CustomerEntity> customerOpt = customerRepository.findById(request.getCustomerId());
        Optional<UserEntity> userOpt = userRepository.findById(request.getUserId());

        if (customerOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(httpRequest, "Customer not found"));
        }
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(httpRequest, "user not found"));
        }

        // 检查时间段是否有冲突
        boolean hasConflict = appointmentRepository.existsByUserIdAndAppointmentDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                request.getUserId(),
                request.getAppointmentDate(),
                request.getEndTime(),
                request.getStartTime()
        );

        if (hasConflict) {
            return ResponseEntity.badRequest().body(new ErrorResponse(httpRequest, "The requested time slot is not available"));
        }

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setCustomer(customerOpt.get());
        appointment.setUser(userOpt.get());
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setStartTime(request.getStartTime());
        appointment.setEndTime(request.getEndTime());
        appointment.setStatus(Const.APPOINTMENT_STATUS_SCHEDULED);
        appointment.setCreateDate(TimeUtils.getNowUTCLocalDateTime());

        appointmentRepository.save(appointment);

        return ResponseEntity.ok(new BaseResponse(httpRequest, "Appointment created successfully"));
    }

    @GetMapping(FunctionPath.appointment.getByCustomerId)
    @Operation(summary = "Get appointments by customer ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AppointmentResponse.GetAll.class)))
    })
    public ResponseEntity<AppointmentResponse.GetAll> getAppointmentsByCustomerId(@PathVariable Long customerId, HttpServletRequest httpRequest) {
        List<AppointmentEntity> appointments = appointmentRepository.findByCustomerId(customerId);
        List<AppointmentResponse.AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(AppointmentResponse.AppointmentDTO::fromEntity)
                .collect(Collectors.toList());

        AppointmentResponse.GetAll response = new AppointmentResponse.GetAll(httpRequest);
        response.setAppointments(appointmentDTOs);
        return ResponseEntity.ok(response);
    }

    // 其他方法如獲取全部預約、更新預約狀態等可以根據需要添加
}
