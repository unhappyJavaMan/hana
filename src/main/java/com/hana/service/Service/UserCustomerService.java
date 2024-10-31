package com.hana.service.Service;

import com.hana.service.DAO.Entity.CustomerEntity;
import com.hana.service.DAO.Entity.UserEntity;
import com.hana.service.DAO.Repository.UserCustomerMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.hana.service.DAO.Entity.UserCustomerMappingEntity;

@Service
public class UserCustomerService {
    @Autowired
    private UserCustomerMappingRepository mappingRepository;

    // 獲取某個用戶關聯的所有客戶
    public List<CustomerEntity> getCustomersByUserId(Long userId) {
        return mappingRepository.findByUserId(userId)
                .stream()
                .map(UserCustomerMappingEntity::getCustomer)
                .collect(Collectors.toList());
    }

    // 獲取某個客戶關聯的所有用戶
    public List<UserEntity> getUsersByCustomerId(Long customerId) {
        return mappingRepository.findByCustomerId(customerId)
                .stream()
                .map(UserCustomerMappingEntity::getUser)
                .collect(Collectors.toList());
    }
}
