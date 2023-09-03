package com.otbs.OnlineTicketBookingSystem.repository;

import com.otbs.OnlineTicketBookingSystem.entity.Role;
import com.otbs.OnlineTicketBookingSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    @Modifying
    @Query("update User u set u.password = ?1 where u.email = ?2")
    int updatePasswordByEmail(@NonNull String password, @NonNull String email);

    Optional<User> findByRole(@NonNull Role role);

    Optional<User> findByEmail(@NonNull String email);

    boolean existsByEmail(@NonNull String email);

    void deleteByEmail(@NonNull String email);


}
