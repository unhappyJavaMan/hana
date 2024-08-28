package com.example.demo.repository;

import com.example.demo.entity.MemberPO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberPO, Long> {
    Optional<MemberPO> findByEmail(String email);
}
