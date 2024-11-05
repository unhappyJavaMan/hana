package com.hana.service.Service;

import com.hana.service.Common.Const;
import com.hana.service.DAO.Entity.UserEntity;
import com.hana.service.DAO.Entity.UserServiceEntity;
import com.hana.service.DAO.Repository.UserRepository;
import com.hana.service.DAO.Repository.UserServiceRepository;
import com.hana.service.Request.UserServiceRequest;
import com.hana.service.Utils.TimeUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceService {
    @Autowired
    private UserServiceRepository userServiceRepository;

    @Autowired
    private UserRepository userRepository;

    public List<UserServiceEntity> getServicesByUserId(Long userId) {
        return userServiceRepository.findByUserIdAndStatus(userId, Const.USER_STATUS_ACTIVE);
    }

    public void createService(Long userId, UserServiceRequest.Create request) {
        UserServiceEntity service = new UserServiceEntity();
        service.setUser(userRepository.getReferenceById(userId));
        service.setServiceName(request.getServiceName());
        service.setDuration(request.getDuration());
        service.setPrice(request.getPrice());
        service.setStatus(Const.USER_STATUS_ACTIVE);
        service.setCreateDate(TimeUtils.getNowUTCLocalDateTime());
        userServiceRepository.save(service);
    }

    public void updateService(Long userId, UserServiceRequest.Update request) {
        UserServiceEntity service = userServiceRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        if (!service.getUser().getId().equals(userId)) {
            throw new RuntimeException("No permission");
        }

        service.setServiceName(request.getServiceName());
        service.setDuration(request.getDuration());
        service.setPrice(request.getPrice());
        service.setUpdateDate(TimeUtils.getNowUTCLocalDateTime());
        userServiceRepository.save(service);
    }

    public void deleteService(Long userId, Long serviceId) {
        UserServiceEntity service = userServiceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        if (!service.getUser().getId().equals(userId)) {
            throw new RuntimeException("No permission");
        }

        service.setStatus(Const.USER_STATUS_REVOKE);
        service.setUpdateDate(TimeUtils.getNowUTCLocalDateTime());
        userServiceRepository.save(service);
    }
}
