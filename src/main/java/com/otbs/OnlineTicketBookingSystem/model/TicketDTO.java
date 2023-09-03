package com.otbs.OnlineTicketBookingSystem.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.List;

@Builder
public record TicketDTO(@Positive @NotNull Long id,
                        @Positive @NotNull Long sessionId,
                        @NotBlank String email,
                        Boolean isUsed,
                        Boolean isPaid,
                        String purchaseTime,
                        String expirationTime,
                        List<SeatDTO> seats) {
}
