package com.transactionAssignment.app.controller;

import com.transactionAssignment.app.dto.TransactionRequestDTO;
import com.transactionAssignment.app.dto.TransactionResponseDTO;
import com.transactionAssignment.app.service.TransactionService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/authorizer")
@Api(value = "Transaction API", tags = {"Transaction"})
public class TransactionController {
    private final TransactionService transactionService;

    @ApiOperation(value = "Authorize a transaction for an MCC", notes = "This endpoint authorizes a transaction based on the provided MCC.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = TransactionResponseDTO.class)
    })
    @PostMapping("/transaction")
    public ResponseEntity<TransactionResponseDTO> authorize(
            @ApiParam(value = "Transaction request details", required = true)
            @Valid @RequestBody TransactionRequestDTO req
    ) {
        try {
            log.info("TransactionController::config req={}", req);
            TransactionResponseDTO res = transactionService.authorize(req);
            log.info("TransactionController::config res={}", res);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            log.error("Transaction::controller - ERROR: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new TransactionResponseDTO("07"));
        }
    }
}
