package com.example.demo.repository;

import com.example.demo.entity.ClockRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ClockRecordRepository extends JpaRepository<ClockRecord, Long> {
    List<ClockRecord> findByMemberId(Long memberId);
    List<ClockRecord> findByMemberIdAndClockInTimeBetween(Long memberId, LocalDateTime startDateTime, LocalDateTime endDateTime);

}
