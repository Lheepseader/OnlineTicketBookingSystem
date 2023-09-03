package com.otbs.OnlineTicketBookingSystem.converter;

import com.otbs.OnlineTicketBookingSystem.constant.ExceptionMessage;
import com.otbs.OnlineTicketBookingSystem.entity.Session;
import com.otbs.OnlineTicketBookingSystem.exception.NotFoundResponseException;
import com.otbs.OnlineTicketBookingSystem.model.SessionDTO;
import com.otbs.OnlineTicketBookingSystem.repository.HallRepository;
import com.otbs.OnlineTicketBookingSystem.repository.MovieRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionConverter implements EntityConverter<Session, SessionDTO> {

    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;

    @Override
    public SessionDTO convert(@NotNull Session session) {
        return SessionDTO.builder()
                .date(session.getDate())
                .graphics(session.getGraphics())
                .movieId(session.getMovie().getId())
                .hallId(session.getHall().getId())
                .build();
    }

    @Override
    public Session convertToEntity(@NotNull SessionDTO dto) {
        return Session.builder()
                .date(dto.date())
                .graphics(dto.graphics())
                .movie(movieRepository.findById(dto.movieId()).orElseThrow(() -> new NotFoundResponseException(
                        ExceptionMessage.NO_SUCH_MOVIE, dto.movieId(), null)))
                .hall(hallRepository.findById(dto.hallId())
                        .orElseThrow(() -> new NotFoundResponseException(ExceptionMessage.NO_SUCH_HALL, dto.hallId())))
                .build();
    }
}
