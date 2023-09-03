package com.otbs.OnlineTicketBookingSystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Builder(toBuilder = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Integer rowPos;

    @Column(nullable = false)
    private Integer columnPos;


    @Column(nullable = false)
    private Boolean isOccupied = false;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Seat seat = (Seat) o;
        return Objects.equals(id, seat.id) && Objects.equals(rowPos, seat.rowPos) && Objects.equals(columnPos,
                seat.columnPos) && Objects.equals(isOccupied, seat.isOccupied) && Objects.equals(session,
                seat.session) && Objects.equals(ticket, seat.ticket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rowPos, columnPos, isOccupied, session, ticket);
    }
}
