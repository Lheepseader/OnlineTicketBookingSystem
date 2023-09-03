package com.otbs.OnlineTicketBookingSystem.repository;

import com.otbs.OnlineTicketBookingSystem.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Transactional
    @Modifying
    @Query("update Ticket t set t.isPaid = ?1, t.purchaseTime = ?2, t.expirationTime = ?3 where t.id = ?4")
    void updateIsPaidAndPurchaseTimeAndExpirationTimeById(@NonNull Boolean isPaid,
                                                          @NonNull LocalDateTime purchaseTime,
                                                          @NonNull LocalDateTime expirationTime,
                                                          Long id);


}
