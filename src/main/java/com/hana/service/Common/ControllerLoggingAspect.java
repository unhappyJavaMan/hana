package com.hana.service.Common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.service.DAO.Entity.OperationLogEntity;
import com.hana.service.DAO.Entity.SystemLogEntity;
import com.hana.service.DAO.Repository.OperationLogRepository;
import com.hana.service.DAO.Repository.SystemLogRepository;
import com.hana.service.DAO.Repository.UserRepository;
import com.hana.service.Enum.OperationEnum;
import com.hana.service.Response.BaseResponse;
import com.hana.service.Utils.LogUtils;
import com.hana.service.Utils.Methods;
import com.hana.service.Utils.TimeUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
public class ControllerLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(ControllerLoggingAspect.class);

//    private static LogUtils logger = new LogUtils();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    OperationLogRepository operationLogRepository;
    @Autowired
    SystemLogRepository systemLogRepository;

    @Autowired
    UserRepository userRepository;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerPointcut() {}

    @Around("restControllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String requestId = Methods.getTransactionInvoiceString();
        request.setAttribute(Const.REQUEST_ID, requestId);

        // Log request
        // Get controller name and method
        String controllerName = joinPoint.getTarget().getClass().getSimpleName();
        LogUtils logger = new LogUtils(controllerName);
        String requestPath = request.getRequestURI();
        logger.info(request, "[" + requestPath +"] input.toString() : " +Arrays.toString(joinPoint.getArgs()));

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        // Log response
        if (result instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            Object body = responseEntity.getBody();
            String jsonResponse = convertObjectToJson(body);

            HttpStatusCode statusCode = ((ResponseEntity<?>) result).getStatusCode();
            String information = null;
            if (statusCode.equals(HttpStatus.OK)){
                information = "success ";
                if (body instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) body;
                    information += baseResponse.getMessage();
                }
            }else {
                information = responseEntity.getBody().toString();
            }

            Optional<OperationEnum> operationEnum = OperationEnum.checkPathReturnType(requestPath);
            if (operationEnum.isPresent()){
                String type = operationEnum.get().getType();
                String logType = operationEnum.get().getLogType();
                if (logType.equals(Const.LOG_TYPE_OPERATION)){
                    String userAccount = Methods.getUserAccountBySecurityContextHolder(SecurityContextHolder.getContext().getAuthentication());
                    OperationLogEntity entity = new OperationLogEntity();
                    entity.setType(type);
                    entity.setTransactionInvoice(requestId);
                    entity.setUserId(userRepository.findByAccount(userAccount).get().getId());
                    entity.setCreateDate(TimeUtils.getNowUTCLocalDateTime());
                    entity.setInformation(information);
                    operationLogRepository.save(entity);
                }else if (logType.equals(Const.LOG_TYPE_SYSTEM)){
                    SystemLogEntity entity = new SystemLogEntity();
                    entity.setType(type);
                    entity.setTransactionInvoice(requestId);
                    entity.setUserAccount(Methods.getUserAccountBySecurityContextHolder(SecurityContextHolder.getContext().getAuthentication()));
                    entity.setCreateDate(TimeUtils.getNowUTCLocalDateTime());
                    entity.setInformation(information);
                    systemLogRepository.save(entity);
                }
            }

            logger.info(request,"Execution Time: {" + executionTime +"}ms, output: " + jsonResponse);

        } else {
            logger.info(request,"Execution Time: {" + executionTime +"}ms, output: " + result);
        }


        return result;
    }


    private String convertObjectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Error converting object to JSON", e);
            return "Unable to convert response to JSON";
        }
    }
}
