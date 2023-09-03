package com.otbs.OnlineTicketBookingSystem.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record SeatDTO(@Positive @NotNull Integer rowPos,
                      @Positive @NotNull Integer columnPos,
                      Boolean isOccupied,
                      @Positive @NotNull Long sessionId,
                      @Positive @NotNull Long ticketId) {

}
