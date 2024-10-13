package com.hana.service.DAO.Repository;

import com.hana.service.DAO.Entity.OperationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLogEntity, Long> {
}

