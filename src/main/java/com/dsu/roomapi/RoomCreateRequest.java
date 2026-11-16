package com.dsu.roomapi;

import jakarta.validation.constraints.*;

public record RoomCreateRequest(
        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must be at most 100 characters")
        String name,

        @Min(value = 1, message = "capacity must be between 1 and 20")
        @Max(value = 20, message = "capacity must be between 1 and 20")
        int capacity) {
}