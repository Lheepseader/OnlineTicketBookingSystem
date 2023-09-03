package com.otbs.OnlineTicketBookingSystem;


import com.otbs.OnlineTicketBookingSystem.constant.ValidMessage;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;


public class SimpleTest {

    public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException {
        SessionDTO sessionDTO = new SessionDTO(null, null, null, null);
        System.out.println(sessionDTO);
    }

    @Builder

    public record SessionDTO(@FutureOrPresent LocalDateTime date,
                             @NotBlank(message = ValidMessage.MANDATORY) String graphics,
                             @Positive Long movieId,
                             @Positive Integer hallId) {
    }

}
