package com.otbs.OnlineTicketBookingSystem.entity;


import com.otbs.OnlineTicketBookingSystem.constant.ValidMessage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(message = "ограничение размера на 30 символов", max = 30)
    @NotNull
    @Column(nullable = false, length = 20)
    private String name;

    @Min(message = "неправильный год (минимум 1900)", value = 1900)
    @NotNull
    @Column(nullable = false)
    private Integer year;

    @ElementCollection(targetClass = Genres.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Genres> genres;

    @Size(message = "Макс. 1000 символов", max = 1000)
    @Column(nullable = false, length = 1000)
    @NotBlank(message = ValidMessage.MANDATORY)
    private String synopsis;

    @NotBlank(message = ValidMessage.MANDATORY)
    @Size(message = "Макс. 150 символов", max = 150)
    @Column(nullable = false, length = 150)
    private String director;

    @NotNull(message = ValidMessage.MANDATORY)
    @Column(nullable = false)
    private Duration duration;


    @Column(nullable = false)
    private String imageFilename;


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id) && Objects.equals(name, movie.name) && Objects.equals(year,
                movie.year) && Objects.equals(genres, movie.genres) && Objects.equals(synopsis,
                movie.synopsis) && Objects.equals(director, movie.director) && Objects.equals(duration,
                movie.duration) && Objects.equals(imageFilename, movie.imageFilename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, year, genres, synopsis, director, duration, imageFilename);
    }
}
