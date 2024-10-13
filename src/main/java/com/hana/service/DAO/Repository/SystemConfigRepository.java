package com.hana.service.DAO.Repository;

import com.hana.service.DAO.Entity.SystemConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfigEntity, Long> {
    SystemConfigEntity findBySystemKey(String key);
}

