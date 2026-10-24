package io.github.meowmeowspence.locklens.service;

import io.github.meowmeowspence.locklens.domain.WorkloadTransaction;
import io.github.meowmeowspence.locklens.repository.WorkloadRepository;
import io.github.meowmeowspence.locklens.repository.WorkloadTransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class WorkloadTransactionService {

    private final WorkloadRepository workloadRepository;
    private final WorkloadTransactionRepository transactionRepository;

    public WorkloadTransactionService(
            WorkloadRepository workloadRepository,
            WorkloadTransactionRepository transactionRepository) {

        this.workloadRepository = workloadRepository;
        this.transactionRepository = transactionRepository;
    }

    public WorkloadTransaction create(
            Long workloadId,
            String name,
            Integer executionOrder) {

        if (!workloadRepository.existsById(workloadId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Workload not found"
            );
        }

        WorkloadTransaction transaction =
                new WorkloadTransaction(
                        workloadId,
                        name,
                        executionOrder
                );

        return transactionRepository.save(transaction);
    }

    public List<WorkloadTransaction> findByWorkload(Long workloadId) {

        if (!workloadRepository.existsById(workloadId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Workload not found"
            );
        }

        return transactionRepository
                .findByWorkloadIdOrderByExecutionOrderAsc(workloadId);
    }
}