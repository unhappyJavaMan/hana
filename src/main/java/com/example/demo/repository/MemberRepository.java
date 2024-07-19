package com.example.demo.repository;

import com.example.demo.entity.MemberPO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberPO, Long> {
}
