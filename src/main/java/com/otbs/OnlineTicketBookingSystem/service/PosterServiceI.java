package com.otbs.OnlineTicketBookingSystem.service;

import com.otbs.OnlineTicketBookingSystem.constant.DateFormat;
import com.otbs.OnlineTicketBookingSystem.constant.ExceptionMessage;
import com.otbs.OnlineTicketBookingSystem.converter.SessionConverter;
import com.otbs.OnlineTicketBookingSystem.entity.Hall;
import com.otbs.OnlineTicketBookingSystem.entity.Movie;
import com.otbs.OnlineTicketBookingSystem.entity.Seat;
import com.otbs.OnlineTicketBookingSystem.entity.Session;
import com.otbs.OnlineTicketBookingSystem.exception.AlreadyExistResponseException;
import com.otbs.OnlineTicketBookingSystem.exception.BadFileException;
import com.otbs.OnlineTicketBookingSystem.exception.NotFoundResponseException;
import com.otbs.OnlineTicketBookingSystem.model.SessionDTO;
import com.otbs.OnlineTicketBookingSystem.repository.HallRepository;
import com.otbs.OnlineTicketBookingSystem.repository.MovieRepository;
import com.otbs.OnlineTicketBookingSystem.repository.SeatRepository;
import com.otbs.OnlineTicketBookingSystem.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class PosterServiceI implements PosterService {

    private final MovieRepository movieRepository;
    private final SessionRepository sessionRepository;
    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;
    private final SessionConverter sessionConverter;
    @Value("${download.path}")
    private String uploadDir;

    @Override
    public String getDateTruncatedTo(ChronoUnit chronoUnit) {
        return LocalDateTime.now().truncatedTo(chronoUnit).format(DateFormat.DATE_FORMATTER);
    }

    @Override
    public String getDateTruncatedTo(LocalDateTime localDateTime, ChronoUnit chronoUnit) {
        return localDateTime.truncatedTo(chronoUnit).format(DateFormat.DATE_FORMATTER);
    }


    @Override
    public Map<Movie, List<Session>> getMovieAndSessionsByTime(LocalDateTime time) {
        List<Session> sessions = sessionRepository.findByDateBetween(time,
                time.withHour(23).withMinute(59).withSecond(59));
        return sessions.stream()
                .collect(Collectors.groupingBy(Session::getMovie));
    }


    @Override
    @Transactional
    public Movie save(Movie movie, MultipartFile file) throws IOException {
        if (file.isEmpty() || !Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            log.info("Incorrect file");
            throw new BadFileException("File is empty or not an image");
        }
        if (movie.getId() != null && movieRepository.existsById(movie.getId())) {
            Files.delete(Path.of(uploadDir, movie.getImageFilename()));
        }
        String filename = generateUniqueFileName(file.getOriginalFilename());
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("Created a directory along the path: " + uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);

        Files.write(filePath, file.getBytes());
        log.info("Image saved - " + filename);
        movie.setImageFilename(filename);
        return movieRepository.save(movie);
    }

    private String generateUniqueFileName(String originalFilename) {
        return System.currentTimeMillis() + "_" + originalFilename;
    }


    @Override
    @Transactional
    public SessionDTO save(SessionDTO sessionDTO) {
        if (!sessionRepository.existsByDateAndHall_Id(sessionDTO.date(), sessionDTO.hallId())) {
            log.info("Session exist by date, hall_id");
            throw new AlreadyExistResponseException(ExceptionMessage.ALREADY_EXIST_SESSION,
                    sessionDTO.toString());
        }
        Hall hall =
                hallRepository.findById(sessionDTO.hallId())
                        .orElseThrow(() -> {
                            log.info("Hall not found by id");
                            return new NotFoundResponseException(ExceptionMessage.NO_SUCH_HALL, sessionDTO.hallId());
                        });

        Session session = sessionConverter.convertToEntity(sessionDTO);

        List<Seat> seats = new ArrayList<>();

        for (int i = 1; i <= hall.getNumRows(); i++) {
            for (int j = 1; j <= hall.getNumColumns(); j++) {
                Seat seat = new Seat().toBuilder()
                        .session(session)
                        .rowPos(i)
                        .columnPos(j)
                        .build();
                seats.add(seat);
            }
        }

        seatRepository.saveAll(seats);
        return sessionConverter.convert(sessionRepository.save(session));
    }

    @Override
    public TreeSet<String> getFutureSessionDates() {
        List<Session> sessions = sessionRepository.findByDateGreaterThanEqual(LocalDateTime.now());

        return sessions.stream()
                .map(Session::getDate)
                .map(date -> getDateTruncatedTo(date, ChronoUnit.DAYS))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public void deleteMovieById(Long id) {
        if (!movieRepository.existsById(id)) {
            log.info("Movie not found by Id");
            throw new NotFoundResponseException(ExceptionMessage.NO_SUCH_MOVIE, id);
        }
        movieRepository.deleteById(id);
    }

    @Override
    public void deleteSessionById(Long id) {
        if (!sessionRepository.existsById(id)) {
            log.info("Session not found by Id");
            throw new NotFoundResponseException(ExceptionMessage.NO_SUCH_SESSION, id, null);
        }
        sessionRepository.deleteById(id);
    }

    @Override
    public SessionDTO getSessionById(Long sessionId) {
        return sessionConverter.convert(sessionRepository.findById(sessionId)
                .orElseThrow(() -> {
                    log.info("Session not found by id");
                    return new NotFoundResponseException(ExceptionMessage.NO_SUCH_SESSION, sessionId, null);
                }));
    }


}