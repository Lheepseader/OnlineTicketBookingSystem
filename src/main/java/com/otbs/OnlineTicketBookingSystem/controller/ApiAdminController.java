package com.otbs.OnlineTicketBookingSystem.controller;

import com.otbs.OnlineTicketBookingSystem.entity.Movie;
import com.otbs.OnlineTicketBookingSystem.model.HallDTO;
import com.otbs.OnlineTicketBookingSystem.model.SessionDTO;
import com.otbs.OnlineTicketBookingSystem.service.HallServiceI;
import com.otbs.OnlineTicketBookingSystem.service.PosterServiceI;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ApiAdminController {

    private final PosterServiceI posterService;

    private final HallServiceI hallService;


    @PostMapping("/save-movie")
    public ResponseEntity<Movie> saveMovie(@RequestPart @Valid final Movie movie,
                                           @RequestPart final MultipartFile img)
            throws IOException {
        return ResponseEntity.ok(posterService.save(movie, img));
    }


    @DeleteMapping("/delete-movie")
    public ResponseEntity<String> deleteMovie(@RequestParam final Long id) {
        posterService.deleteMovieById(id);
        return new ResponseEntity<>("Фильм удалён ", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/save-session")
    public ResponseEntity<SessionDTO> saveSession(@RequestParam @Valid final SessionDTO session) {
        return ResponseEntity.ok(posterService.save(session));
    }


    @DeleteMapping("/delete-session")
    public ResponseEntity<String> deleteSession(@RequestParam final Long id) {
        posterService.deleteSessionById(id);
        return new ResponseEntity<>("Сеанс удалён ", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/save-hall")
    public ResponseEntity<HallDTO> saveHall(@RequestBody @Valid final HallDTO hall) {
        return new ResponseEntity<>(hallService.save(hall), HttpStatus.OK);
    }


    @DeleteMapping("/delete-hall")
    public ResponseEntity<String> deleteHall(@RequestParam final Integer id) {
        hallService.deleteById(id);
        return new ResponseEntity<>("Зал удалён ", HttpStatus.NO_CONTENT);
    }


}
