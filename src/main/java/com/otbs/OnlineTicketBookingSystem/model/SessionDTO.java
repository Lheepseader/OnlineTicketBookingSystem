package com.otbs.OnlineTicketBookingSystem.model;

import com.otbs.OnlineTicketBookingSystem.constant.ValidMessage;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SessionDTO(@FutureOrPresent @NotNull LocalDateTime date,
                         @NotBlank(message = ValidMessage.MANDATORY) String graphics,
                         @Positive @NotNull Long movieId,
                         @Positive @NotNull Integer hallId) {
}
