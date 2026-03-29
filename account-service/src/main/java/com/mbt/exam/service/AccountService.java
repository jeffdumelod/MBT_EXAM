package com.mbt.exam.service;

import com.mbt.exam.dto.AccountCreationRequest;
import com.mbt.exam.dto.AccountCreationResponse;
import com.mbt.exam.dto.CustomerInquiryResponse;
import com.mbt.exam.exception.CustomerNotFoundException;
import com.mbt.exam.model.Account;
import com.mbt.exam.model.Customer;
import com.mbt.exam.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final CustomerRepository customerRepository;

    @Transactional
    public AccountCreationResponse createCustomerAccount(AccountCreationRequest request) {
        Customer customer = Customer.builder()
                .customerName(request.getCustomerName())
                .customerMobile(request.getCustomerMobile())
                .customerEmail(request.getCustomerEmail())
                .address1(request.getAddress1())
                .address2(request.getAddress2())
                .build();

        Account account = Account.builder()
                .accountType(request.getAccountType())
                .availableBalance(new BigDecimal("500.00")) // Default initial balance
                .build();

        customer.addAccount(account);

        Customer savedCustomer = customerRepository.save(customer);

        return AccountCreationResponse.builder()
                .customerNumber(savedCustomer.getCustomerNumber())
                .transactionStatusCode(201)
                .transactionStatusDescription("Customer account created")
                .build();
    }

    @Transactional(readOnly = true)
    public CustomerInquiryResponse getCustomerAccount(Long customerNumber) {
        Customer customer = customerRepository.findById(customerNumber)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        List<CustomerInquiryResponse.AccountDto> accountDtos = customer.getSavings().stream()
                .map(acc -> CustomerInquiryResponse.AccountDto.builder()
                        .accountNumber(acc.getAccountNumber())
                        .accountType(acc.getAccountType().getDescription())
                        .availableBalance(acc.getAvailableBalance())
                        .build())
                .collect(Collectors.toList());

        return CustomerInquiryResponse.builder()
                .customerNumber(customer.getCustomerNumber())
                .customerName(customer.getCustomerName())
                .customerMobile(customer.getCustomerMobile())
                .customerEmail(customer.getCustomerEmail())
                .address1(customer.getAddress1())
                .address2(customer.getAddress2())
                .savings(accountDtos)
                .transactionStatusCode(302)
                .transactionStatusDescription("Customer Account found")
                .build();
    }
}
