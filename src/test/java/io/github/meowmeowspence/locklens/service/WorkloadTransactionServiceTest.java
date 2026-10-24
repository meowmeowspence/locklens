package io.github.meowmeowspence.locklens.service;

import io.github.meowmeowspence.locklens.domain.Workload;
import io.github.meowmeowspence.locklens.domain.WorkloadTransaction;
import io.github.meowmeowspence.locklens.repository.WorkloadRepository;
import io.github.meowmeowspence.locklens.repository.WorkloadTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class WorkloadTransactionServiceTest {

    @Autowired
    private WorkloadService workloadService;

    @Autowired
    private WorkloadTransactionService transactionService;

    @Autowired
    private WorkloadRepository workloadRepository;

    @Autowired
    private WorkloadTransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        workloadRepository.deleteAll();
    }

    @Test
    void createPersistsTransactionForWorkload() {
        Workload workload = workloadService.create(
                "Deadlock Test",
                "Test workload"
        );

        WorkloadTransaction transaction =
                transactionService.create(
                        workload.getId(),
                        "Transaction A",
                        0
                );

        assertNotNull(transaction.getId());
        assertEquals(workload.getId(), transaction.getWorkloadId());
        assertEquals("Transaction A", transaction.getName());
        assertEquals(0, transaction.getExecutionOrder());

        assertTrue(
                transactionRepository.findById(transaction.getId()).isPresent()
        );
    }

    @Test
    void findByWorkloadReturnsTransactionsInExecutionOrder() {
        Workload workload = workloadService.create(
                "Deadlock Test",
                "Test workload"
        );

        transactionService.create(
                workload.getId(),
                "Second",
                1
        );

        transactionService.create(
                workload.getId(),
                "First",
                0
        );

        List<WorkloadTransaction> transactions =
                transactionService.findByWorkload(workload.getId());

        assertEquals(2, transactions.size());
        assertEquals("First", transactions.get(0).getName());
        assertEquals("Second", transactions.get(1).getName());
    }

    @Test
    void createReturnsNotFoundForUnknownWorkload() {
        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> transactionService.create(
                                Long.MAX_VALUE,
                                "Transaction A",
                                0
                        )
                );

        assertEquals(
                HttpStatus.NOT_FOUND,
                exception.getStatusCode()
        );
    }
}