package com.hana.service.DAO.Repository;

import com.hana.service.DAO.Entity.UserCustomerMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCustomerMappingRepository extends JpaRepository<UserCustomerMappingEntity, Long> {
    List<UserCustomerMappingEntity> findByUserId(Long userId);
    List<UserCustomerMappingEntity> findByCustomerId(Long customerId);
}
