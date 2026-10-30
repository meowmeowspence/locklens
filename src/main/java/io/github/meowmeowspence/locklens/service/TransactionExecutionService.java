package io.github.meowmeowspence.locklens.service;

import io.github.meowmeowspence.locklens.domain.TransactionExecutionStatus;
import io.github.meowmeowspence.locklens.domain.TransactionOperation;
import io.github.meowmeowspence.locklens.domain.WorkloadTransaction;
import io.github.meowmeowspence.locklens.dto.TransactionExecutionResponse;
import io.github.meowmeowspence.locklens.repository.TransactionOperationRepository;
import io.github.meowmeowspence.locklens.repository.WorkloadTransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Service
public class TransactionExecutionService {

    private final DataSource dataSource;
    private final WorkloadTransactionRepository transactionRepository;
    private final TransactionOperationRepository operationRepository;

    public TransactionExecutionService(
            DataSource dataSource,
            WorkloadTransactionRepository transactionRepository,
            TransactionOperationRepository operationRepository) {

        this.dataSource = dataSource;
        this.transactionRepository = transactionRepository;
        this.operationRepository = operationRepository;
    }

    public TransactionExecutionResponse execute(
            Long workloadId,
            Long transactionId) {

        WorkloadTransaction transaction =
                transactionRepository
                        .findByIdAndWorkloadId(transactionId, workloadId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Transaction not found for workload"
                        ));

        List<TransactionOperation> operations =
                operationRepository
                        .findByWorkloadTransactionIdOrderByExecutionOrderAsc(
                                transactionId
                        );

        try (Connection connection = dataSource.getConnection()) {

            connection.setAutoCommit(false);

            int executedOperations = 0;

            try (Statement statement = connection.createStatement()) {

                for (TransactionOperation operation : operations) {
                    statement.execute(operation.getSqlStatement());
                    executedOperations++;
                }

                connection.commit();

                return new TransactionExecutionResponse(
                        transaction.getId(),
                        transaction.getName(),
                        TransactionExecutionStatus.COMMITTED,
                        executedOperations,
                        null
                );

            } catch (SQLException executionException) {

                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    executionException.addSuppressed(rollbackException);
                }

                return new TransactionExecutionResponse(
                        transaction.getId(),
                        transaction.getName(),
                        TransactionExecutionStatus.ROLLED_BACK,
                        executedOperations,
                        executionException.getMessage()
                );
            }

        } catch (SQLException connectionException) {

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to execute transaction",
                    connectionException
            );
        }
    }
}