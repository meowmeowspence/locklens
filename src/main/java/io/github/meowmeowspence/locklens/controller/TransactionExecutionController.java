package io.github.meowmeowspence.locklens.controller;

import io.github.meowmeowspence.locklens.dto.TransactionExecutionResponse;
import io.github.meowmeowspence.locklens.service.TransactionExecutionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        "/api/workloads/{workloadId}/transactions/{transactionId}"
)
public class TransactionExecutionController {

    private final TransactionExecutionService executionService;

    public TransactionExecutionController(
            TransactionExecutionService executionService) {

        this.executionService = executionService;
    }

    @PostMapping("/execute")
    public TransactionExecutionResponse executeTransaction(
            @PathVariable Long workloadId,
            @PathVariable Long transactionId) {

        return executionService.execute(
                workloadId,
                transactionId
        );
    }
}