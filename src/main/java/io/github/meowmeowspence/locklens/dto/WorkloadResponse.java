package io.github.meowmeowspence.locklens.dto;

import io.github.meowmeowspence.locklens.domain.Workload;

import java.time.Instant;

public record WorkloadResponse(
        Long id,
        String name,
        String description,
        Instant createdAt
) {

    public static WorkloadResponse from(Workload workload) {
        return new WorkloadResponse(
                workload.getId(),
                workload.getName(),
                workload.getDescription(),
                workload.getCreatedAt()
        );
    }
}