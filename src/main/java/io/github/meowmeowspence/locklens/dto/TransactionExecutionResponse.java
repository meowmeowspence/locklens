package io.github.meowmeowspence.locklens.dto;

import io.github.meowmeowspence.locklens.domain.TransactionExecutionStatus;

public record TransactionExecutionResponse(
        Long transactionId,
        String transactionName,
        TransactionExecutionStatus status,
        int executedOperations,
        String errorMessage
) {
}