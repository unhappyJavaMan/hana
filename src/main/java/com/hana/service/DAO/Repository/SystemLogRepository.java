package com.hana.service.DAO.Repository;

import com.hana.service.DAO.Entity.SystemLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLogEntity, Long> {
}

