package com.otbs.OnlineTicketBookingSystem.repository;

import com.otbs.OnlineTicketBookingSystem.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    boolean existsByNameAndYearAndDirectorAndDuration(String name, Integer year, String director, Duration duration);


}
