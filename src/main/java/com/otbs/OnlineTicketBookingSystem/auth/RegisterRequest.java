package com.otbs.OnlineTicketBookingSystem.auth;


import com.otbs.OnlineTicketBookingSystem.constant.ValidMessage;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotNull(message = ValidMessage.MANDATORY)
    private String email;
    @NotNull(message = ValidMessage.MANDATORY)
    private String password;
}
