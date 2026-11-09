package com.dsu.roomapi;

import java.time.LocalDateTime;

public record ReservationResponse(Long id, Long roomId, String reservedBy,
                                  LocalDateTime startTime, LocalDateTime endTime) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getRoom().getId(),
                reservation.getReservedBy(), reservation.getStartTime(), reservation.getEndTime());
    }
}
