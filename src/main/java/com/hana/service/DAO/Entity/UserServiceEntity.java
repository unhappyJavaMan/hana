package com.hana.service.DAO.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_services")
@Data
public class UserServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String serviceName;  // 服務名稱

    @Column(nullable = false)
    private Integer duration;    // 服務時間（分鐘）

    @Column(nullable = false)
    private BigDecimal price;    // 服務價錢

    @Column(nullable = false)
    private String status;      // 狀態（活動/關閉）

    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
