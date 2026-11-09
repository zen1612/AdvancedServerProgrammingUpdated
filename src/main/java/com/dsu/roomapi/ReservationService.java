package com.dsu.roomapi;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.*;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservations;
    private final RoomRepository rooms;

    public ReservationService(ReservationRepository reservations, RoomRepository rooms) {
        this.reservations = reservations;
        this.rooms = rooms;
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findByRoom(Long roomId) {
        if (!rooms.existsById(roomId)) throw new ResponseStatusException(NOT_FOUND, "Room not found");
        return reservations.findByRoomIdOrderByStartTime(roomId).stream()
                .map(ReservationResponse::from).toList();
    }

    public ReservationResponse create(ReservationCreateRequest request) {
        if (request.roomId() == null || request.reservedBy() == null || request.reservedBy().isBlank() || request.reservedBy().length() > 50
                || request.startTime() == null || request.endTime() == null
                || !request.endTime().isAfter(request.startTime())) {
            throw new ResponseStatusException(BAD_REQUEST, "Room, name and valid start/end times are required");
        }
        // Serialize bookings for the same room so concurrent requests cannot double-book it.
        Room room = rooms.findForReservation(request.roomId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Room not found"));
        if (reservations.existsByRoomIdAndStartTimeLessThanAndEndTimeGreaterThan(
                room.getId(), request.endTime(), request.startTime())) {
            throw new ResponseStatusException(CONFLICT, "Room is already reserved for this time");
        }
        return ReservationResponse.from(reservations.save(new Reservation(null, room,
                request.reservedBy(), request.startTime(), request.endTime())));
    }

    public void delete(Long id) {
        Reservation reservation = reservations.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Reservation not found"));
        reservations.delete(reservation);
    }
}
