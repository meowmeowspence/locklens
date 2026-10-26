package io.github.meowmeowspence.locklens.controller;

import io.github.meowmeowspence.locklens.domain.TransactionOperation;
import io.github.meowmeowspence.locklens.dto.CreateTransactionOperationRequest;
import io.github.meowmeowspence.locklens.dto.TransactionOperationResponse;
import io.github.meowmeowspence.locklens.service.TransactionOperationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        "/api/workloads/{workloadId}/transactions/{transactionId}/operations"
)
public class TransactionOperationController {

    private final TransactionOperationService operationService;

    public TransactionOperationController(
            TransactionOperationService operationService) {

        this.operationService = operationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionOperationResponse createOperation(
            @PathVariable Long workloadId,
            @PathVariable Long transactionId,
            @Valid @RequestBody CreateTransactionOperationRequest request) {

        TransactionOperation operation =
                operationService.create(
                        workloadId,
                        transactionId,
                        request.sqlStatement(),
                        request.executionOrder()
                );

        return TransactionOperationResponse.from(operation);
    }

    @GetMapping
    public List<TransactionOperationResponse> getOperations(
            @PathVariable Long workloadId,
            @PathVariable Long transactionId) {

        return operationService
                .findByTransaction(workloadId, transactionId)
                .stream()
                .map(TransactionOperationResponse::from)
                .toList();
    }
}