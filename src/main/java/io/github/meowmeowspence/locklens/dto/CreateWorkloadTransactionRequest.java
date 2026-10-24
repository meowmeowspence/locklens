package io.github.meowmeowspence.locklens.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateWorkloadTransactionRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be 100 characters or fewer")
        String name,

        @NotNull(message = "Execution order is required")
        @Min(value = 0, message = "Execution order cannot be negative")
        Integer executionOrder

) {
}