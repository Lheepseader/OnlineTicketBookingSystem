package com.otbs.OnlineTicketBookingSystem.service;

import com.otbs.OnlineTicketBookingSystem.auth.AuthenticationRequest;
import com.otbs.OnlineTicketBookingSystem.auth.AuthenticationResponse;
import com.otbs.OnlineTicketBookingSystem.auth.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
