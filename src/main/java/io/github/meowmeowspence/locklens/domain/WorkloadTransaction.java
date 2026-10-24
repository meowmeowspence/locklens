package io.github.meowmeowspence.locklens.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "workload_transactions")
public class WorkloadTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workload_id", nullable = false)
    private Long workloadId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "execution_order", nullable = false)
    private Integer executionOrder;

    protected WorkloadTransaction() {
    }

    public WorkloadTransaction(
            Long workloadId,
            String name,
            Integer executionOrder) {

        this.workloadId = workloadId;
        this.name = name;
        this.executionOrder = executionOrder;
    }

    public Long getId() {
        return id;
    }

    public Long getWorkloadId() {
        return workloadId;
    }

    public String getName() {
        return name;
    }

    public Integer getExecutionOrder() {
        return executionOrder;
    }
}