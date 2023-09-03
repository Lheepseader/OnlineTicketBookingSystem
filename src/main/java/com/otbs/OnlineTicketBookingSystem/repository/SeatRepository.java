package com.otbs.OnlineTicketBookingSystem.repository;

import com.otbs.OnlineTicketBookingSystem.entity.Seat;
import com.otbs.OnlineTicketBookingSystem.entity.Session;
import com.otbs.OnlineTicketBookingSystem.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findBySession_IdAndRowPosAndColumnPos(@NonNull Long id,
                                                         @NonNull Integer rowPos,
                                                         @NonNull Integer columnPos);

    @Transactional
    @Modifying
    @Query("""
            update Seat s set s.isOccupied = ?1, s.ticket = ?2
            where s.rowPos = ?3 and s.columnPos = ?4 and s.session = ?5""")
    void updateIsOccupiedAndTicketByRowPosAndColumnPosAndSession(@NonNull Boolean isOccupied,
                                                                 @NonNull Ticket ticket,
                                                                 @NonNull Integer rowPos,
                                                                 @NonNull Integer columnPos,
                                                                 @NonNull Session session);


}
