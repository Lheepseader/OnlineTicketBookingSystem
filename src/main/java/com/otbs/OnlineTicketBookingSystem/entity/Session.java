package com.otbs.OnlineTicketBookingSystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@ToString
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false, length = 2)
    private String graphics;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;


    @ManyToOne(optional = false)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Seat> seats;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Ticket> tickets;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Session session = (Session) o;
        return Objects.equals(id, session.id) && Objects.equals(date, session.date) && Objects.equals(graphics,
                session.graphics) && Objects.equals(movie, session.movie) && Objects.equals(hall,
                session.hall) && Objects.equals(seats, session.seats) && Objects.equals(tickets, session.tickets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, graphics, movie, hall, seats, tickets);
    }
}
