package com.dsu.roomapi;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findByRoom(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new NotFoundException("room " + roomId + " not found");
        }
        return reservationRepository.findByRoomIdOrderByStartTime(roomId).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse create(Long roomId, String reservedBy,
                                      LocalDateTime startTime, LocalDateTime endTime) {
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("startTime must be before endTime");
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("room " + roomId + " not found"));

        if (reservationRepository.existsByRoomIdAndStartTimeLessThanAndEndTimeGreaterThan(
                roomId, endTime, startTime)) {
            throw new ConflictException("room " + roomId + " is already booked at that time");
        }

        return ReservationResponse.from(reservationRepository.save(
                new Reservation(null, room, reservedBy, startTime, endTime)));
    }

    @Transactional(readOnly = true)
    public ReservationResponse get(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("reservation " + id + " not found"));
        return ReservationResponse.from(reservation);
    }

    @Transactional
    public void delete(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("reservation " + id + " not found"));
        reservationRepository.delete(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findByReservedBy(String reservedBy) {
        return reservationRepository.findByReservedByOrderByStartTime(reservedBy).stream()
                .map(ReservationResponse::from)
                .toList();
    }
}