package io.github.meowmeowspence.locklens.dto;

import io.github.meowmeowspence.locklens.domain.WorkloadTransaction;

public record WorkloadTransactionResponse(
        Long id,
        Long workloadId,
        String name,
        Integer executionOrder
) {

    public static WorkloadTransactionResponse from(
            WorkloadTransaction transaction) {

        return new WorkloadTransactionResponse(
                transaction.getId(),
                transaction.getWorkloadId(),
                transaction.getName(),
                transaction.getExecutionOrder()
        );
    }
}