package io.github.meowmeowspence.locklens.service;

import io.github.meowmeowspence.locklens.domain.TransactionOperation;
import io.github.meowmeowspence.locklens.domain.Workload;
import io.github.meowmeowspence.locklens.domain.WorkloadTransaction;
import io.github.meowmeowspence.locklens.repository.TransactionOperationRepository;
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
class TransactionOperationServiceTest {

    @Autowired
    private WorkloadService workloadService;

    @Autowired
    private WorkloadTransactionService transactionService;

    @Autowired
    private TransactionOperationService operationService;

    @Autowired
    private WorkloadRepository workloadRepository;

    @Autowired
    private WorkloadTransactionRepository transactionRepository;

    @Autowired
    private TransactionOperationRepository operationRepository;

    @BeforeEach
    void setUp() {
        operationRepository.deleteAll();
        transactionRepository.deleteAll();
        workloadRepository.deleteAll();
    }

    @Test
    void createPersistsOperationForTransaction() {
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

        TransactionOperation operation =
                operationService.create(
                        workload.getId(),
                        transaction.getId(),
                        "UPDATE accounts SET balance = 0 WHERE id = 1",
                        0
                );

        assertNotNull(operation.getId());
        assertEquals(
                transaction.getId(),
                operation.getWorkloadTransactionId()
        );
        assertEquals(0, operation.getExecutionOrder());

        assertTrue(
                operationRepository.findById(operation.getId()).isPresent()
        );
    }

    @Test
    void findByTransactionReturnsOperationsInExecutionOrder() {
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

        operationService.create(
                workload.getId(),
                transaction.getId(),
                "SECOND",
                1
        );

        operationService.create(
                workload.getId(),
                transaction.getId(),
                "FIRST",
                0
        );

        List<TransactionOperation> operations =
                operationService.findByTransaction(
                        workload.getId(),
                        transaction.getId()
                );

        assertEquals(2, operations.size());
        assertEquals("FIRST", operations.get(0).getSqlStatement());
        assertEquals("SECOND", operations.get(1).getSqlStatement());
    }

    @Test
    void createRejectsTransactionFromDifferentWorkload() {
        Workload firstWorkload =
                workloadService.create(
                        "First",
                        "First workload"
                );

        Workload secondWorkload =
                workloadService.create(
                        "Second",
                        "Second workload"
                );

        WorkloadTransaction transaction =
                transactionService.create(
                        firstWorkload.getId(),
                        "Transaction A",
                        0
                );

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> operationService.create(
                                secondWorkload.getId(),
                                transaction.getId(),
                                "SELECT 1",
                                0
                        )
                );

        assertEquals(
                HttpStatus.NOT_FOUND,
                exception.getStatusCode()
        );
    }

    @Test
    void createRejectsDuplicateExecutionOrder() {
        Workload workload =
                workloadService.create(
                        "Deadlock Test",
                        "Test workload"
                );

        WorkloadTransaction transaction =
                transactionService.create(
                        workload.getId(),
                        "Transaction A",
                        0
                );

        operationService.create(
                workload.getId(),
                transaction.getId(),
                "FIRST",
                0
        );

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> operationService.create(
                                workload.getId(),
                                transaction.getId(),
                                "DUPLICATE",
                                0
                        )
                );

        assertEquals(
                HttpStatus.CONFLICT,
                exception.getStatusCode()
        );
    }
}