package com.otbs.OnlineTicketBookingSystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Builder
public class Hall {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private Integer numRows;

    @Column(nullable = false)
    private Integer numColumns;


    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Session> sessions;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Hall hall = (Hall) o;
        return Objects.equals(id, hall.id) && Objects.equals(numRows, hall.numRows) && Objects.equals(numColumns,
                hall.numColumns) && Objects.equals(sessions, hall.sessions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numRows, numColumns, sessions);
    }
}
