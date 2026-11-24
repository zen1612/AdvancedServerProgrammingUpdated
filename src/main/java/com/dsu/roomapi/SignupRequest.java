package com.dsu.roomapi;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public record SignupRequest(
        @NotBlank
        @Pattern(regexp = "[a-z0-9]{3,20}",
                message = "username must be 3 to 20 lowercase letters or digits")
        String username,
        @NotBlank
        @Size(min = 8, max = 72, message = "password must be 8 to 72 characters")
        String password
) {}
