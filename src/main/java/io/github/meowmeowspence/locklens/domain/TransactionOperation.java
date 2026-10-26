package io.github.meowmeowspence.locklens.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transaction_operations")
public class TransactionOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workload_transaction_id", nullable = false)
    private Long workloadTransactionId;

    @Column(name = "sql_statement", nullable = false, columnDefinition = "TEXT")
    private String sqlStatement;

    @Column(name = "execution_order", nullable = false)
    private Integer executionOrder;

    protected TransactionOperation() {
    }

    public TransactionOperation(
            Long workloadTransactionId,
            String sqlStatement,
            Integer executionOrder) {

        this.workloadTransactionId = workloadTransactionId;
        this.sqlStatement = sqlStatement;
        this.executionOrder = executionOrder;
    }

    public Long getId() {
        return id;
    }

    public Long getWorkloadTransactionId() {
        return workloadTransactionId;
    }

    public String getSqlStatement() {
        return sqlStatement;
    }

    public Integer getExecutionOrder() {
        return executionOrder;
    }
}