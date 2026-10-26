package io.github.meowmeowspence.locklens.repository;

import io.github.meowmeowspence.locklens.domain.TransactionOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionOperationRepository
        extends JpaRepository<TransactionOperation, Long> {

    List<TransactionOperation>
    findByWorkloadTransactionIdOrderByExecutionOrderAsc(
            Long workloadTransactionId
    );

    boolean existsByWorkloadTransactionIdAndExecutionOrder(
            Long workloadTransactionId,
            Integer executionOrder
    );
}