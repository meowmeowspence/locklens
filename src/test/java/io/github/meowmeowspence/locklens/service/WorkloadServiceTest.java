package io.github.meowmeowspence.locklens.service;

import io.github.meowmeowspence.locklens.domain.Workload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WorkloadServiceTest {

    private WorkloadService workloadService;

    @BeforeEach
    void setUp() {
        workloadService = new WorkloadService();
    }

    @Test
    void createAssignsIdAndStoresWorkload() {
        Workload workload = workloadService.create(
                "Deadlock Test",
                "Test description"
        );

        assertEquals(1L, workload.getId());
        assertEquals("Deadlock Test", workload.getName());
        assertEquals("Test description", workload.getDescription());
        assertNotNull(workload.getCreatedAt());

        Optional<Workload> stored = workloadService.findById(1L);

        assertTrue(stored.isPresent());
        assertEquals("Deadlock Test", stored.get().getName());
    }

    @Test
    void findAllReturnsWorkloadsInIdOrder() {
        workloadService.create("First", "First workload");
        workloadService.create("Second", "Second workload");

        List<Workload> workloads = workloadService.findAll();

        assertEquals(2, workloads.size());
        assertEquals(1L, workloads.get(0).getId());
        assertEquals(2L, workloads.get(1).getId());
    }

    @Test
    void findByIdReturnsEmptyForUnknownWorkload() {
        Optional<Workload> result = workloadService.findById(999L);

        assertTrue(result.isEmpty());
    }
}