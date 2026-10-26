package io.github.meowmeowspence.locklens.dto;

import io.github.meowmeowspence.locklens.domain.TransactionOperation;

public record TransactionOperationResponse(
        Long id,
        Long workloadTransactionId,
        String sqlStatement,
        Integer executionOrder
) {

    public static TransactionOperationResponse from(
            TransactionOperation operation) {

        return new TransactionOperationResponse(
                operation.getId(),
                operation.getWorkloadTransactionId(),
                operation.getSqlStatement(),
                operation.getExecutionOrder()
        );
    }
}