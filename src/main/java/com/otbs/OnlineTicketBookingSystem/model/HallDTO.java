package com.otbs.OnlineTicketBookingSystem.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record HallDTO(
        @Positive @NotNull Integer id,
        @Positive @NotNull Integer numRows,
        @Positive @NotNull Integer numColumns) {
}
