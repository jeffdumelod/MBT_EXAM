package com.mbt.exam.service;

import com.mbt.exam.dto.AccountCreationRequest;
import com.mbt.exam.dto.AccountCreationResponse;
import com.mbt.exam.dto.CustomerInquiryResponse;
import com.mbt.exam.exception.CustomerNotFoundException;
import com.mbt.exam.model.Account;
import com.mbt.exam.model.AccountType;
import com.mbt.exam.model.Customer;
import com.mbt.exam.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void testCreateCustomerAccount() {
        AccountCreationRequest request = new AccountCreationRequest();
        request.setCustomerName("Test");
        request.setCustomerMobile("09081234567");
        request.setCustomerEmail("test12345@gmail.com");
        request.setAddress1("test");
        request.setAccountType(AccountType.S);

        Customer mockCustomer = Customer.builder().customerNumber(12345678L).build();
        when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);

        AccountCreationResponse response = accountService.createCustomerAccount(request);

        assertNotNull(response);
        assertEquals(12345678L, response.getCustomerNumber());
        assertEquals(201, response.getTransactionStatusCode());
    }

    @Test
    public void testGetCustomerAccount_Found() {
        Account mockAccount = Account.builder()
                .accountNumber(10001L)
                .accountType(AccountType.S)
                .availableBalance(new BigDecimal("500"))
                .build();

        Customer mockCustomer = Customer.builder()
                .customerNumber(12345678L)
                .customerName("Test")
                .savings(Collections.singletonList(mockAccount))
                .build();

        when(customerRepository.findById(12345678L)).thenReturn(Optional.of(mockCustomer));

        CustomerInquiryResponse response = accountService.getCustomerAccount(12345678L);

        assertNotNull(response);
        assertEquals(12345678L, response.getCustomerNumber());
        assertEquals(302, response.getTransactionStatusCode());
        assertFalse(response.getSavings().isEmpty());
    }

    @Test
    public void testGetCustomerAccount_NotFound() {
        when(customerRepository.findById(9999L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            accountService.getCustomerAccount(9999L);
        });
    }
}
