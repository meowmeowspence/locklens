package io.github.meowmeowspence.locklens.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTransactionOperationRequest(

        @NotBlank(message = "SQL statement is required")
        @Size(
                max = 10000,
                message = "SQL statement must be 10000 characters or fewer"
        )
        String sqlStatement,

        @NotNull(message = "Execution order is required")
        @Min(
                value = 0,
                message = "Execution order cannot be negative"
        )
        Integer executionOrder

) {
}