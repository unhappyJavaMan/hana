package com.hana.service.DAO.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "SystemConfig")
public class SystemConfigEntity {
    @Id
    @Column(nullable = false)
    private String systemKey;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String systemValue;

    @Column(nullable = true)
    private long updateUser;

    @Column(nullable = true)
    private LocalDateTime updateDate;


}
