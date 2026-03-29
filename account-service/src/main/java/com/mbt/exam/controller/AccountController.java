package com.mbt.exam.controller;

import com.mbt.exam.dto.AccountCreationRequest;
import com.mbt.exam.dto.AccountCreationResponse;
import com.mbt.exam.dto.CustomerInquiryResponse;
import com.mbt.exam.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountCreationResponse> createAccount(@Valid @RequestBody AccountCreationRequest request) {
        AccountCreationResponse response = accountService.createCustomerAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{customerNumber}")
    public ResponseEntity<CustomerInquiryResponse> getAccount(@PathVariable Long customerNumber) {
        CustomerInquiryResponse response = accountService.getCustomerAccount(customerNumber);
        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }
}
