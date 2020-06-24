package com.phones.phones.repository;

import com.phones.phones.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    @Modifying
    @Query(
            value = "UPDATE users u " +
                    "SET u.is_active = false " +
                    "WHERE u.id = ?1",
            nativeQuery = true
    )
    int disableById(Long id);


    @Query(
            value = "SELECT count(u.id) " +
                    "FROM users u " +
                    "WHERE u.username = ?1",
            nativeQuery = true
    )
    int findByUsernameBoolean(String username);

    Optional<User> findByDni(String dni);

    Optional<User> findByUsername(String username);

}
