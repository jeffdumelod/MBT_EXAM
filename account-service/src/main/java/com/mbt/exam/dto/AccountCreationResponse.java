package com.mbt.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreationResponse {
    private Long customerNumber;
    private int transactionStatusCode;
    private String transactionStatusDescription;
}
