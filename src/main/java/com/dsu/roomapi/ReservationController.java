package com.dsu.roomapi;

import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping("/api/rooms/{roomId}/reservations")
    public List<ReservationResponse> list(@PathVariable Long roomId) {
        return service.findByRoom(roomId);
    }

    @PostMapping("/api/rooms/{roomId}/reservations")
    public ResponseEntity<ReservationResponse> create(
            @PathVariable Long roomId, @Valid @RequestBody ReservationCreateRequest request) {
        ReservationResponse reservation = service.create(
                roomId, request.reservedBy(), request.startTime(), request.endTime());
        return ResponseEntity.created(URI.create("/api/reservations/" + reservation.id()))
                .body(reservation);
    }

    @GetMapping("/api/reservations/{id}")
    public ReservationResponse findOne(@PathVariable Long id) {
        return service.get(id);
    }

    @DeleteMapping("/api/reservations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/api/reservations")
    public List<ReservationResponse> search(@RequestParam String reservedBy) {
        return service.findByReservedBy(reservedBy);
    }
}