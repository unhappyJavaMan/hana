package com.example.demo.controller;

import com.example.demo.entity.ClockRecord;
import com.example.demo.repository.ClockRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/clock-records")
public class ClockRecordController {

    @Autowired
    private ClockRecordRepository clockRecordRepository;

    @PostMapping("/clock-in")
    public ResponseEntity<ClockRecord> clockIn(@RequestParam Long memberId) {
        ClockRecord clockRecord = new ClockRecord();
        clockRecord.setMemberId(memberId);
        clockRecord.setClockInTime(LocalDateTime.now());
        ClockRecord savedRecord = clockRecordRepository.save(clockRecord);
        return ResponseEntity.ok(savedRecord);
    }

    @PutMapping("/clock-out/{id}")
    public ResponseEntity<ClockRecord> clockOut(@PathVariable Long id) {
        ClockRecord clockRecord = clockRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clock record not found with ID: " + id));
        clockRecord.setClockOutTime(LocalDateTime.now());
        ClockRecord updatedRecord = clockRecordRepository.save(clockRecord);
        return ResponseEntity.ok(updatedRecord);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ClockRecord>> getClockRecordsByMemberId(@PathVariable Long memberId) {
        List<ClockRecord> clockRecords = clockRecordRepository.findByMemberId(memberId);
        return ResponseEntity.ok(clockRecords);
    }

    @GetMapping("/today/{memberId}")
    public ResponseEntity<List<ClockRecord>> getTodayClockRecords(@PathVariable Long memberId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        List<ClockRecord> todayRecords = clockRecordRepository.findByMemberIdAndClockInTimeBetween(memberId, startOfDay, endOfDay);
        return ResponseEntity.ok(todayRecords);
    }
}
