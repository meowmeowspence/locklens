package io.github.meowmeowspence.locklens.service;

import io.github.meowmeowspence.locklens.domain.Workload;
import io.github.meowmeowspence.locklens.repository.WorkloadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class WorkloadServiceTest {

    @Autowired
    private WorkloadService workloadService;

    @Autowired
    private WorkloadRepository workloadRepository;

    @BeforeEach
    void setUp() {
        workloadRepository.deleteAll();
    }

    @Test
    void createPersistsWorkload() {
        Workload workload = workloadService.create(
                "Deadlock Test",
                "Test description"
        );

        assertNotNull(workload.getId());
        assertEquals("Deadlock Test", workload.getName());
        assertEquals("Test description", workload.getDescription());
        assertNotNull(workload.getCreatedAt());

        Optional<Workload> stored =
                workloadRepository.findById(workload.getId());

        assertTrue(stored.isPresent());
        assertEquals("Deadlock Test", stored.get().getName());
    }

    @Test
    void findAllReturnsWorkloadsInIdOrder() {
        Workload first = workloadService.create(
                "First",
                "First workload"
        );

        Workload second = workloadService.create(
                "Second",
                "Second workload"
        );

        List<Workload> workloads = workloadService.findAll();

        assertEquals(2, workloads.size());
        assertEquals(first.getId(), workloads.get(0).getId());
        assertEquals(second.getId(), workloads.get(1).getId());
    }

    @Test
    void findByIdReturnsEmptyForUnknownWorkload() {
        Optional<Workload> result =
                workloadService.findById(Long.MAX_VALUE);

        assertTrue(result.isEmpty());
    }
}