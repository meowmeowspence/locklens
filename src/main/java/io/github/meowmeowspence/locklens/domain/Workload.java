package io.github.meowmeowspence.locklens.domain;

import java.time.Instant;

public class Workload {

    private final Long id;
    private final String name;
    private final String description;
    private final Instant createdAt;

    public Workload(Long id, String name, String description, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}