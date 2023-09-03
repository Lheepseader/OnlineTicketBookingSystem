package com.otbs.OnlineTicketBookingSystem.service;

import com.otbs.OnlineTicketBookingSystem.entity.Movie;
import com.otbs.OnlineTicketBookingSystem.entity.Session;
import com.otbs.OnlineTicketBookingSystem.model.SessionDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface PosterService {

    String getDateTruncatedTo(ChronoUnit chronoUnit);

    String getDateTruncatedTo(LocalDateTime localDateTime, ChronoUnit chronoUnit);


    Map<Movie, List<Session>> getMovieAndSessionsByTime(LocalDateTime time);

    Movie save(Movie movie, MultipartFile file) throws IOException;


    SessionDTO save(SessionDTO sessionDTO);


    TreeSet<String> getFutureSessionDates();

    void deleteMovieById(Long id);

    void deleteSessionById(Long id);

    SessionDTO getSessionById(Long sessionId);
}
