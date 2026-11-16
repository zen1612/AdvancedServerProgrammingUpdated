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
    public ResponseEntity<List<ReservationResponse>> list(
            @PathVariable Long roomId) {
        return service.findByRoom(roomId)
                .map(reservations -> ResponseEntity.ok(
                        reservations.stream()
                                .map(ReservationResponse::from)
                                .toList()
                ))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/api/rooms/{roomId}/reservations")
    public ResponseEntity<ReservationResponse> create(
            @PathVariable Long roomId, @RequestBody ReservationCreateRequest request) {
        try {
            return service.create(roomId, request.reservedBy(), request.startTime(), request.endTime())
                    .map(r -> ResponseEntity.created(URI.create("/api/reservations/" + r.getId()))
                            .body(ReservationResponse.from(r)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/api/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/reservations")
    public List<ReservationResponse> search(
            @RequestParam String reservedBy) {
        return service.findByReservedBy(reservedBy);
    }
}
