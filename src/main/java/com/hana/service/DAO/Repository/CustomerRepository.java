package com.hana.service.DAO.Repository;

import com.hana.service.DAO.Entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
