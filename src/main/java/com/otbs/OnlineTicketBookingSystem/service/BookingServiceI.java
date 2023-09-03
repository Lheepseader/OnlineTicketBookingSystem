package com.otbs.OnlineTicketBookingSystem.service;

import com.otbs.OnlineTicketBookingSystem.constant.ExceptionMessage;
import com.otbs.OnlineTicketBookingSystem.entity.Seat;
import com.otbs.OnlineTicketBookingSystem.entity.Session;
import com.otbs.OnlineTicketBookingSystem.entity.Ticket;
import com.otbs.OnlineTicketBookingSystem.entity.User;
import com.otbs.OnlineTicketBookingSystem.exception.NotFoundResponseException;
import com.otbs.OnlineTicketBookingSystem.exception.PaymentException;
import com.otbs.OnlineTicketBookingSystem.model.SeatDTO;
import com.otbs.OnlineTicketBookingSystem.model.TicketDTO;
import com.otbs.OnlineTicketBookingSystem.repository.SeatRepository;
import com.otbs.OnlineTicketBookingSystem.repository.SessionRepository;
import com.otbs.OnlineTicketBookingSystem.repository.TicketRepository;
import com.otbs.OnlineTicketBookingSystem.request.BookingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceI implements BookingService {

    private final TicketRepository ticketRepository;
    private final SessionRepository sessionRepository;
    private final SeatRepository seatRepository;


    @Transactional
    @Override
    public Ticket createReservation(Long sessionId, User user, List<Seat> seat) {
        Ticket ticket =
                new Ticket().toBuilder()
                        .session(sessionRepository.findById(sessionId).orElseThrow(() -> new NotFoundResponseException(
                                ExceptionMessage.NO_SUCH_SESSION, sessionId, null)))
                        .user(user)
                        .expirationTime((LocalDateTime.now().plusMinutes(15)))
                        .seats(seat)
                        .build();

        return ticketRepository.save(ticket);
    }


    @Override
    public List<Seat> getSeatsBySessionIdAndSeatsDto(Long sessionId, List<SeatDTO> seatDTOList) {
        List<Seat> seats = new ArrayList<>();
        for (SeatDTO seatDTO : seatDTOList) {
            seats.add(seatRepository.findBySession_IdAndRowPosAndColumnPos(sessionId, seatDTO.rowPos(),
                    seatDTO.columnPos()).orElseThrow(
                    () -> new NotFoundResponseException(ExceptionMessage.NO_SUCH_SEAT, seatDTO.toString())));
        }
        return seats;
    }

    @Transactional
    @Override
    public Long buyTicket(BookingRequest request, User user) {
        Ticket ticket;
        List<Seat> seats = getSeatsBySessionIdAndSeatsDto(request.getSessionId(), request.getSeatDTOList());
        if (request.getTicketId() == null) {
            ticket = createReservation(request.getSessionId(), user, seats);
        } else {
            ticket = ticketRepository.findById(request.getTicketId())
                    .orElse(createReservation(request.getSessionId(), user, seats));
        }
        if (!doPayment()) {
            throw new PaymentException();
        }

        Session session =
                sessionRepository.findById(request.getSessionId())
                        .orElseThrow(() -> new NotFoundResponseException(ExceptionMessage.NO_SUCH_SESSION,
                                request.getSessionId(),
                                null));

        ticketRepository.updateIsPaidAndPurchaseTimeAndExpirationTimeById(true,
                LocalDateTime.now(),
                session.getDate().plusHours(4),
                ticket.getId());

        for (Seat seat : seats) {
            seatRepository.updateIsOccupiedAndTicketByRowPosAndColumnPosAndSession(true,
                    ticket,
                    seat.getRowPos(),
                    seat.getColumnPos(),
                    session);
        }
        return ticket.getId();
    }

    @Transactional
    @Override
    public void cancelReservation(TicketDTO ticketDTO) {
        Ticket ticket =
                ticketRepository.findById(ticketDTO.id())
                        .orElseThrow(() -> new NotFoundResponseException(ExceptionMessage.NO_SUCH_TICKET,
                                ticketDTO.id()));
        for (Seat seat : ticket.getSeats()) {
            seatRepository.updateIsOccupiedAndTicketByRowPosAndColumnPosAndSession(false,
                    ticket,
                    seat.getRowPos(),
                    seat.getColumnPos(),
                    ticket.getSession()
            );
        }
        ticketRepository.delete(ticket);
    }

    @Override
    public boolean doPayment() {
        return true;
    }
}
