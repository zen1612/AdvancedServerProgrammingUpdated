package com.dsu.roomapi;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<RoomResponse> list(@RequestParam(required = false) Integer minCapacity,
                                   @RequestParam(defaultValue = "") String keyword) {
        return roomService.search(minCapacity, keyword).stream()
                .map(RoomResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> findOne(@PathVariable Long id) {
        return roomService.findById(id)
                .map(room -> ResponseEntity.ok(RoomResponse.from(room)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoomResponse> create(@RequestBody RoomCreateRequest request) {
        try {
            Room room = roomService.create(request.name(), request.capacity());
            return ResponseEntity.created(URI.create("/api/rooms/" + room.getId()))
                    .body(RoomResponse.from(room));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> replace(@PathVariable Long id, @RequestBody RoomCreateRequest request) {
        try {
            Room room = roomService.update(id, request.name(), request.capacity());
            return ResponseEntity.ok(RoomResponse.from(room));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            return roomService.delete(id)
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }
    }
}