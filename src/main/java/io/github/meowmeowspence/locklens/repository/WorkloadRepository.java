package io.github.meowmeowspence.locklens.repository;

import io.github.meowmeowspence.locklens.domain.Workload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkloadRepository extends JpaRepository<Workload, Long> {
}