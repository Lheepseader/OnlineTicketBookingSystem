package com.otbs.OnlineTicketBookingSystem.converter;

import com.otbs.OnlineTicketBookingSystem.constant.DateFormat;
import com.otbs.OnlineTicketBookingSystem.constant.ExceptionMessage;
import com.otbs.OnlineTicketBookingSystem.entity.Ticket;
import com.otbs.OnlineTicketBookingSystem.exception.NotFoundResponseException;
import com.otbs.OnlineTicketBookingSystem.model.TicketDTO;
import com.otbs.OnlineTicketBookingSystem.repository.SessionRepository;
import com.otbs.OnlineTicketBookingSystem.repository.UserRepository;
import com.otbs.OnlineTicketBookingSystem.service.BookingService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TicketConverter implements EntityConverter<Ticket, TicketDTO> {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SeatConverter seatConverter;
    private final BookingService bookingService;


    @Override
    public Ticket convertToEntity(@NotNull TicketDTO dto) {
        return Ticket.builder()
                .id(dto.id())
                .session(sessionRepository.findById(dto.sessionId())
                        .orElseThrow(() -> new NotFoundResponseException(ExceptionMessage.NO_SUCH_SESSION,
                                dto.sessionId(),
                                null)))
                .user(userRepository.findByEmail(dto.email())
                        .orElseThrow(() -> new NotFoundResponseException(ExceptionMessage.NO_SUCH_USER,
                                dto.email())))
                .isUsed(dto.isUsed())
                .isPaid(dto.isPaid())
                .purchaseTime(LocalDateTime.parse(dto.purchaseTime(), DateFormat.DATE_HOUR_MINUTE_FORMATTER))
                .expirationTime(LocalDateTime.parse(dto.expirationTime(), DateFormat.DATE_HOUR_MINUTE_FORMATTER))
                .seats(bookingService.getSeatsBySessionIdAndSeatsDto(dto.sessionId(), dto.seats()))
                .build();
    }


    @Override
    public TicketDTO convert(@NotNull Ticket ticket) {
        return TicketDTO.builder()
                .id(ticket.getId())
                .sessionId(ticket.getSession().getId())
                .email(ticket.getUser().getEmail())
                .isUsed(ticket.getIsUsed())
                .isPaid(ticket.getIsPaid())
                .purchaseTime(ticket.getPurchaseTime().format(DateFormat.DATE_HOUR_MINUTE_FORMATTER))
                .expirationTime(ticket.getExpirationTime().format(DateFormat.DATE_HOUR_MINUTE_FORMATTER))
                .seats(ticket.getSeats().stream().map(seatConverter::convert).toList())
                .build();
    }


}
