package com.dsu.roomapi;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    // empty = no such room (404)
    public Optional<List<Reservation>> findByRoom(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            return Optional.empty();
        }

        return Optional.of(reservationRepository
                .findByRoomIdOrderByStartTime(roomId));
    }

    @Transactional
    public Optional<Reservation> create(
            Long roomId,
            String reservedBy,
            LocalDateTime startTime,
            LocalDateTime endTime) {
        checkRequest(reservedBy, startTime, endTime); //400
        Optional<Room> room = roomRepository.findById(roomId);
        if (room.isEmpty()) {
            return Optional.empty(); //404
        }
        return Optional.of(reservationRepository.save(
                new Reservation(
                        null,
                        room.get(),
                        reservedBy,
                        startTime,
                        endTime
                )
        ));
    }

    private void checkRequest(
            String reservedBy,
            LocalDateTime startTime,
            LocalDateTime endTime) {

        if (reservedBy == null) {
            throw new IllegalArgumentException(
                    "reservedBy is required and must be at most 50 characters"
            );
        }

        if (startTime == null
                || endTime == null
                || !startTime.isBefore(endTime)) {
            throw new IllegalArgumentException(
                    "startTime must be before endTime"
            );
        }
    }

    @Transactional
    public void delete(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND,
                        "Reservation not found"
                ));

        reservationRepository.delete(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findByReservedBy(String reservedBy) {
        return reservationRepository
                .findByReservedByOrderByStartTime(reservedBy)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }
}