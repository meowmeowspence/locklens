package io.github.meowmeowspence.locklens.service;

import io.github.meowmeowspence.locklens.domain.TransactionExecutionStatus;
import io.github.meowmeowspence.locklens.domain.Workload;
import io.github.meowmeowspence.locklens.domain.WorkloadTransaction;
import io.github.meowmeowspence.locklens.dto.TransactionExecutionResponse;
import io.github.meowmeowspence.locklens.repository.TransactionOperationRepository;
import io.github.meowmeowspence.locklens.repository.WorkloadRepository;
import io.github.meowmeowspence.locklens.repository.WorkloadTransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionExecutionServiceTest {

    @Autowired
    private WorkloadService workloadService;

    @Autowired
    private WorkloadTransactionService transactionService;

    @Autowired
    private TransactionOperationService operationService;

    @Autowired
    private TransactionExecutionService executionService;

    @Autowired
    private WorkloadRepository workloadRepository;

    @Autowired
    private WorkloadTransactionRepository transactionRepository;

    @Autowired
    private TransactionOperationRepository operationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        operationRepository.deleteAll();
        transactionRepository.deleteAll();
        workloadRepository.deleteAll();
        resetAccounts();
    }

    @AfterEach
    void tearDown() {
        operationRepository.deleteAll();
        transactionRepository.deleteAll();
        workloadRepository.deleteAll();
        resetAccounts();
    }

    @Test
    void executeCommitsAllOperations() {
        Workload workload = workloadService.create(
                "Execution Test",
                "Tests committing operations"
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
                "UPDATE accounts SET balance = balance - 50 WHERE id = 1",
                0
        );

        operationService.create(
                workload.getId(),
                transaction.getId(),
                "UPDATE accounts SET balance = balance + 50 WHERE id = 2",
                1
        );

        TransactionExecutionResponse result =
                executionService.execute(
                        workload.getId(),
                        transaction.getId()
                );

        assertEquals(
                TransactionExecutionStatus.COMMITTED,
                result.status()
        );

        assertEquals(2, result.executedOperations());

        assertEquals(950L, getBalance(1L));
        assertEquals(1050L, getBalance(2L));
    }

    @Test
    void executeRollsBackAllOperationsWhenOneFails() {
        Workload workload = workloadService.create(
                "Rollback Test",
                "Tests rollback behavior"
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
                "UPDATE accounts SET balance = balance - 100 WHERE id = 1",
                0
        );

        operationService.create(
                workload.getId(),
                transaction.getId(),
                "UPDATE table_that_does_not_exist SET value = 1",
                1
        );

        TransactionExecutionResponse result =
                executionService.execute(
                        workload.getId(),
                        transaction.getId()
                );

        assertEquals(
                TransactionExecutionStatus.ROLLED_BACK,
                result.status()
        );

        assertEquals(1, result.executedOperations());

        assertNotNull(result.errorMessage());

        assertEquals(1000L, getBalance(1L));
    }

    @Test
    void executeRejectsTransactionFromDifferentWorkload() {
        Workload firstWorkload = workloadService.create(
                "First",
                "First workload"
        );

        Workload secondWorkload = workloadService.create(
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
                        () -> executionService.execute(
                                secondWorkload.getId(),
                                transaction.getId()
                        )
                );

        assertEquals(
                HttpStatus.NOT_FOUND,
                exception.getStatusCode()
        );
    }

    private Long getBalance(Long accountId) {
        return jdbcTemplate.queryForObject(
                "SELECT balance FROM accounts WHERE id = ?",
                Long.class,
                accountId
        );
    }

    private void resetAccounts() {
        jdbcTemplate.update(
                "UPDATE accounts SET balance = 1000 WHERE id IN (1, 2)"
        );
    }
}