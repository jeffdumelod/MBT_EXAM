package com.mbt.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInquiryResponse {
    private Long customerNumber;
    private String customerName;
    private String customerMobile;
    private String customerEmail;
    private String address1;
    private String address2;
    private List<AccountDto> savings;
    private int transactionStatusCode;
    private String transactionStatusDescription;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountDto {
        private Long accountNumber;
        private String accountType; // "Savings" or "Checking"
        private BigDecimal availableBalance;
    }
}
