package io.github.meowmeowspence.locklens.service;

import io.github.meowmeowspence.locklens.domain.Workload;
import io.github.meowmeowspence.locklens.repository.WorkloadRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class WorkloadService {

    private final WorkloadRepository workloadRepository;

    public WorkloadService(WorkloadRepository workloadRepository) {
        this.workloadRepository = workloadRepository;
    }

    public Workload create(String name, String description) {
        Workload workload = new Workload(
                name,
                description,
                Instant.now()
        );

        return workloadRepository.save(workload);
    }

    public List<Workload> findAll() {
        return workloadRepository.findAll(
                Sort.by(Sort.Direction.ASC, "id")
        );
    }

    public Optional<Workload> findById(Long id) {
        return workloadRepository.findById(id);
    }
}