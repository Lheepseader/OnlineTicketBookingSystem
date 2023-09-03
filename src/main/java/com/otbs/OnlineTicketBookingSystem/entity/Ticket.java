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
@ToString
@Entity
@Builder(toBuilder = true)
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Column(nullable = false)
    private Boolean isUsed = false;

    @Column(nullable = false)
    private Boolean isPaid = false;

    @Column
    private LocalDateTime purchaseTime;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Seat> seats;

    public Ticket(Session session, User user, LocalDateTime expirationTime) {
        this.session = session;
        this.user = user;
        this.expirationTime = expirationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) && Objects.equals(session, ticket.session) && Objects.equals(user,
                ticket.user) && Objects.equals(isUsed, ticket.isUsed) && Objects.equals(purchaseTime,
                ticket.purchaseTime) && Objects.equals(expirationTime, ticket.expirationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, session, user, isUsed, purchaseTime, expirationTime);
    }
}
