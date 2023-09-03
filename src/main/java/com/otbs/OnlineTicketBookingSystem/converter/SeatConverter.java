package com.otbs.OnlineTicketBookingSystem.converter;

import com.otbs.OnlineTicketBookingSystem.constant.ExceptionMessage;
import com.otbs.OnlineTicketBookingSystem.entity.Seat;
import com.otbs.OnlineTicketBookingSystem.exception.NotFoundResponseException;
import com.otbs.OnlineTicketBookingSystem.model.SeatDTO;
import com.otbs.OnlineTicketBookingSystem.repository.SessionRepository;
import com.otbs.OnlineTicketBookingSystem.repository.TicketRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatConverter implements EntityConverter<Seat, SeatDTO> {

    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;

    @Override
    public Seat convertToEntity(@NotNull SeatDTO dto) {
        return Seat.builder()
                .rowPos(dto.rowPos())
                .columnPos(dto.columnPos())
                .isOccupied(dto.isOccupied())
                .session(sessionRepository.findById(dto.sessionId())
                        .orElseThrow(() -> new NotFoundResponseException(ExceptionMessage.NO_SUCH_SESSION,
                                dto.sessionId())))
                .ticket(ticketRepository.findById(dto.ticketId()).orElseThrow(() -> new NotFoundResponseException(
                        ExceptionMessage.NO_SUCH_TICKET, dto.ticketId(), null)))
                .build();
    }

    @Override
    public SeatDTO convert(@NotNull Seat seat) {
        return SeatDTO.builder()
                .rowPos(seat.getRowPos())
                .columnPos(seat.getColumnPos())
                .isOccupied(seat.getIsOccupied())
                .sessionId(seat.getSession().getId())
                .ticketId(seat.getTicket().getId())
                .build();
    }
}
