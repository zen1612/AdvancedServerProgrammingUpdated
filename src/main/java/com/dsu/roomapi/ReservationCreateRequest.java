package com.dsu.roomapi;

import java.time.LocalDateTime;

public record ReservationCreateRequest(Long roomId, String reservedBy,
                                       LocalDateTime startTime, LocalDateTime endTime) {}
