package io.github.meowmeowspence.locklens.service;

import io.github.meowmeowspence.locklens.domain.TransactionOperation;
import io.github.meowmeowspence.locklens.repository.TransactionOperationRepository;
import io.github.meowmeowspence.locklens.repository.WorkloadTransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TransactionOperationService {

    private final WorkloadTransactionRepository transactionRepository;
    private final TransactionOperationRepository operationRepository;

    public TransactionOperationService(
            WorkloadTransactionRepository transactionRepository,
            TransactionOperationRepository operationRepository) {

        this.transactionRepository = transactionRepository;
        this.operationRepository = operationRepository;
    }

    public TransactionOperation create(
            Long workloadId,
            Long transactionId,
            String sqlStatement,
            Integer executionOrder) {

        if (!transactionRepository.existsByIdAndWorkloadId(
                transactionId,
                workloadId)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Transaction not found for workload"
            );
        }

        if (operationRepository
                .existsByWorkloadTransactionIdAndExecutionOrder(
                        transactionId,
                        executionOrder)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Execution order already exists for transaction"
            );
        }

        TransactionOperation operation =
                new TransactionOperation(
                        transactionId,
                        sqlStatement,
                        executionOrder
                );

        return operationRepository.save(operation);
    }

    public List<TransactionOperation> findByTransaction(
            Long workloadId,
            Long transactionId) {

        if (!transactionRepository.existsByIdAndWorkloadId(
                transactionId,
                workloadId)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Transaction not found for workload"
            );
        }

        return operationRepository
                .findByWorkloadTransactionIdOrderByExecutionOrderAsc(
                        transactionId
                );
    }
}