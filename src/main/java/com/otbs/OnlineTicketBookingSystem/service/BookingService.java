package com.otbs.OnlineTicketBookingSystem.service;


import com.otbs.OnlineTicketBookingSystem.entity.Seat;
import com.otbs.OnlineTicketBookingSystem.entity.Ticket;
import com.otbs.OnlineTicketBookingSystem.entity.User;
import com.otbs.OnlineTicketBookingSystem.model.SeatDTO;
import com.otbs.OnlineTicketBookingSystem.model.TicketDTO;
import com.otbs.OnlineTicketBookingSystem.request.BookingRequest;

import java.util.List;

public interface BookingService {

    Ticket createReservation(Long sessionId, User user, List<Seat> seats);

    List<Seat> getSeatsBySessionIdAndSeatsDto(Long sessionId, List<SeatDTO> seatDTOList);

    Long buyTicket(BookingRequest request, User user);

    void cancelReservation(TicketDTO ticketDTO);

    boolean doPayment();
}
