package com.hana.service.DAO.Repository;

import com.hana.service.DAO.Entity.UserServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserServiceRepository extends JpaRepository<UserServiceEntity, Long> {
    @Query("SELECT s FROM UserServiceEntity s " +
            "WHERE s.user.id = :userId AND s.status = :status")
    List<UserServiceEntity> findByUserIdAndStatus(@Param("userId") Long userId,
                                                  @Param("status") String status);

}
