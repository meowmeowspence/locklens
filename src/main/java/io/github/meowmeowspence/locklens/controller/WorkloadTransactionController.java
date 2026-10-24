package io.github.meowmeowspence.locklens.controller;

import io.github.meowmeowspence.locklens.domain.WorkloadTransaction;
import io.github.meowmeowspence.locklens.dto.CreateWorkloadTransactionRequest;
import io.github.meowmeowspence.locklens.dto.WorkloadTransactionResponse;
import io.github.meowmeowspence.locklens.service.WorkloadTransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workloads/{workloadId}/transactions")
public class WorkloadTransactionController {

    private final WorkloadTransactionService transactionService;

    public WorkloadTransactionController(
            WorkloadTransactionService transactionService) {

        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkloadTransactionResponse createTransaction(
            @PathVariable Long workloadId,
            @Valid @RequestBody CreateWorkloadTransactionRequest request) {

        WorkloadTransaction transaction =
                transactionService.create(
                        workloadId,
                        request.name(),
                        request.executionOrder()
                );

        return WorkloadTransactionResponse.from(transaction);
    }

    @GetMapping
    public List<WorkloadTransactionResponse> getTransactions(
            @PathVariable Long workloadId) {

        return transactionService.findByWorkload(workloadId)
                .stream()
                .map(WorkloadTransactionResponse::from)
                .toList();
    }
}