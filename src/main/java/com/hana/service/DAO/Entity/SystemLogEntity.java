package com.hana.service.DAO.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "SystemLog")
public class SystemLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userAccount;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String information;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @Column(nullable = false)
    private String transactionInvoice;

}
