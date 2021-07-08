package com.kma.practice8.springsecuritycustom.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kma.practice8.springsecuritycustom.domain.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Query("SELECT user FROM UserEntity user "
        + "LEFT JOIN FETCH user.role role "
        + "LEFT JOIN FETCH role.permissions "
        + "WHERE user.login = :login")
    Optional<UserEntity> findByLogin(@Param("login") String login);

}
