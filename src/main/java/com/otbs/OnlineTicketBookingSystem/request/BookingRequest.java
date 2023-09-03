package com.otbs.OnlineTicketBookingSystem.request;

import com.otbs.OnlineTicketBookingSystem.model.SeatDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {

    @Positive
    private Long ticketId;

    @Positive
    private Long sessionId;

    @NotNull
    private List<SeatDTO> seatDTOList;
}
