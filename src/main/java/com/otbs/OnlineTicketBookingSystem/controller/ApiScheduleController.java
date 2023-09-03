package com.otbs.OnlineTicketBookingSystem.controller;

import com.otbs.OnlineTicketBookingSystem.entity.User;
import com.otbs.OnlineTicketBookingSystem.model.SessionDTO;
import com.otbs.OnlineTicketBookingSystem.request.BookingRequest;
import com.otbs.OnlineTicketBookingSystem.response.ScheduleResponse;
import com.otbs.OnlineTicketBookingSystem.service.BookingService;
import com.otbs.OnlineTicketBookingSystem.service.PosterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ApiScheduleController {

    private final PosterService posterService;
    private final BookingService bookingService;

    @GetMapping("/sessions")
    public ResponseEntity<ScheduleResponse> getSessions(@RequestParam(name = "date", required = false)
                                                        @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDateTime date) {
        if (date == null) {
            date = LocalDateTime.now();
        }

        return ResponseEntity.ok(ScheduleResponse.builder()
                .movieSessionsMap(posterService.getMovieAndSessionsByTime(date))
                .futureSessionDates(posterService.getFutureSessionDates())
                .build());

    }


    @GetMapping("/session")
    public ResponseEntity<SessionDTO> getSessionById(@RequestParam(name = "session") Long sessionId) {

        return ResponseEntity.ok(posterService.getSessionById(sessionId));
    }

    @PostMapping("/session/buy")
    public ResponseEntity<Long> buyTicketOnSessionById(@RequestBody @Valid BookingRequest request,
                                                       @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(bookingService.buyTicket(request, user));
    }


}
