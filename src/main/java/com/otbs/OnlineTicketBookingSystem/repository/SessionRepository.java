package com.otbs.OnlineTicketBookingSystem.repository;

import com.otbs.OnlineTicketBookingSystem.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    boolean existsByDateAndHall_Id(LocalDateTime date, Integer id);

    List<Session> findByDateBetween(@NonNull LocalDateTime dateStart, @NonNull LocalDateTime dateEnd);

    List<Session> findByDateGreaterThanEqual(@NonNull LocalDateTime date);


}
