package com.hana.service.DAO.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "User")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickName;
    @Column(nullable = false)
    private String account;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private LocalDateTime createDate;
    @Column(nullable = true)
    private LocalDateTime updateDate;
    @Column(nullable = true, columnDefinition = "TEXT")
    private String loginToken;
    @Column(nullable = true)
    private LocalDateTime tokenExpiredTime;


}
