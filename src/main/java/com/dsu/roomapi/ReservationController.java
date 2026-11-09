package com.dsu.roomapi;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservationController {
    private final ReservationService service;
    public ReservationController(ReservationService service) { this.service = service; }

    @GetMapping("/api/rooms/{roomId}/reservations")
    public List<ReservationResponse> list(@PathVariable Long roomId) {
        return service.findByRoom(roomId);
    }

    @PostMapping("/api/reservations")
    public ResponseEntity<ReservationResponse> create(@RequestBody ReservationCreateRequest request) {
        ReservationResponse reservation = service.create(request);
        return ResponseEntity.created(URI.create("/api/reservations/" + reservation.id())).body(reservation);
    }

    @DeleteMapping("/api/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
