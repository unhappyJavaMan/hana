package com.hana.service.DAO.Entity;


import com.hana.service.Common.Const;
import com.hana.service.Request.CustomerRequest;
import com.hana.service.Utils.TimeUtils;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
@Data
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String phone;

    @Column(nullable = false)
    private String status;

    @Column(nullable = true)
    private String gender;

    @Column(nullable = true)  // 允許為空，因為可能有些客戶不願提供生日
    private LocalDate birthDate;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "customer")
    private Set<UserCustomerMappingEntity> userMappings = new HashSet<>();

    public static CustomerEntity  fromCreateByUserId(CustomerRequest.Create request){
        CustomerEntity customer = new CustomerEntity();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setBirthDate(request.getBirthDate());
        customer.setGender(request.getGender());
        customer.setStatus(Const.USER_STATUS_ACTIVE);
        customer.setCreateDate(TimeUtils.getNowUTCLocalDateTime());
        return customer;
    }
}
