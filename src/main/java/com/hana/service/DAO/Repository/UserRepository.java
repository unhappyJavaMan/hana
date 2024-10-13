package com.hana.service.DAO.Repository;

import com.hana.service.DAO.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Component

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByAccount(String account);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<UserEntity> findByAccount(String account);
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByLoginToken(String loginToken);
    Optional<UserEntity> findByAccountAndLoginToken(String account, String loginToken);
}

