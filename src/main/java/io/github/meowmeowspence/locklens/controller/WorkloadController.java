package io.github.meowmeowspence.locklens.controller;

import io.github.meowmeowspence.locklens.domain.Workload;
import io.github.meowmeowspence.locklens.dto.CreateWorkloadRequest;
import io.github.meowmeowspence.locklens.dto.WorkloadResponse;
import io.github.meowmeowspence.locklens.service.WorkloadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/workloads")
public class WorkloadController {

    private final WorkloadService workloadService;

    public WorkloadController(WorkloadService workloadService) {
        this.workloadService = workloadService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkloadResponse createWorkload(
            @Valid @RequestBody CreateWorkloadRequest request) {

        Workload workload = workloadService.create(
                request.name(),
                request.description()
        );

        return WorkloadResponse.from(workload);
    }

    @GetMapping
    public List<WorkloadResponse> getWorkloads() {
        return workloadService.findAll()
                .stream()
                .map(WorkloadResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public WorkloadResponse getWorkload(@PathVariable Long id) {

        Workload workload = workloadService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Workload not found"
                ));

        return WorkloadResponse.from(workload);
    }
}