package io.github.meowmeowspence.locklens.repository;

import io.github.meowmeowspence.locklens.domain.WorkloadTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkloadTransactionRepository
        extends JpaRepository<WorkloadTransaction, Long> {

    List<WorkloadTransaction>
    findByWorkloadIdOrderByExecutionOrderAsc(Long workloadId);

    boolean existsByIdAndWorkloadId(Long id, Long workloadId);
}