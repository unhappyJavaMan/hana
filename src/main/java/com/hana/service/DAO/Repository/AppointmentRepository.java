package com.hana.service.DAO.Repository;

import com.hana.service.DAO.Entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    List<AppointmentEntity> findByCustomerId(Long customerId);

    boolean existsByUserIdAndAppointmentDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            Long userId, LocalDate appointmentDate, LocalTime endTime, LocalTime startTime);
}
