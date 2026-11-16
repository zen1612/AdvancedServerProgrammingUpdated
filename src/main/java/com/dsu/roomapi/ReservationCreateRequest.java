package com.dsu.roomapi;

import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

public record ReservationCreateRequest(
        @NotBlank(message = "reservedBy is required")
        @Size(max = 50, message = "reservedBy must be at most 50 characters")
        String reservedBy,
        @NotNull(message = "startTime is required") LocalDateTime startTime,
        @NotNull(message = "endTime is required") LocalDateTime endTime) {}