package com.dsu.roomapi;

import java.time.LocalDateTime;

public record ReservationResponse(Long id, Long roomId, String reservedBy,
                                  LocalDateTime startTime, LocalDateTime endTime) {
    public static ReservationResponse from(Reservation r) {
        return new ReservationResponse(r.getId(), r.getRoom().getId(),
                r.getReservedBy(), r.getStartTime(), r.getEndTime());
    }
}
