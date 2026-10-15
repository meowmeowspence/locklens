package io.github.meowmeowspence.locklens.service;

import io.github.meowmeowspence.locklens.domain.Workload;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class WorkloadService {

    private final Map<Long, Workload> workloads = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public Workload create(String name, String description) {
        long id = nextId.getAndIncrement();

        Workload workload = new Workload(
                id,
                name,
                description,
                Instant.now()
        );

        workloads.put(id, workload);

        return workload;
    }

    public List<Workload> findAll() {
        return workloads.values()
                .stream()
                .sorted(Comparator.comparing(Workload::getId))
                .toList();
    }

    public Optional<Workload> findById(Long id) {
        return Optional.ofNullable(workloads.get(id));
    }
}