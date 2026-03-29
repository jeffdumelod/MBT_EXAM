package com.mbt.exam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbt.exam.dto.AccountCreationRequest;
import com.mbt.exam.dto.AccountCreationResponse;
import com.mbt.exam.dto.CustomerInquiryResponse;
import com.mbt.exam.exception.CustomerNotFoundException;
import com.mbt.exam.model.AccountType;
import com.mbt.exam.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateAccount_Success() throws Exception {
        AccountCreationRequest request = new AccountCreationRequest();
        request.setCustomerName("Test");
        request.setCustomerMobile("09081234567");
        request.setCustomerEmail("test12345@gmail.com");
        request.setAddress1("test");
        request.setAddress2("test");
        request.setAccountType(AccountType.S);

        AccountCreationResponse response = AccountCreationResponse.builder()
                .customerNumber(12345678L)
                .transactionStatusCode(201)
                .transactionStatusDescription("Customer account created")
                .build();

        when(accountService.createCustomerAccount(any(AccountCreationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerNumber").value(12345678))
                .andExpect(jsonPath("$.transactionStatusCode").value(201))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Customer account created"));
    }

    @Test
    public void testCreateAccount_Failed_MissingEmail() throws Exception {
        AccountCreationRequest request = new AccountCreationRequest();
        request.setCustomerName("Test");
        request.setCustomerMobile("09081234567");
        request.setCustomerEmail(""); // triggers @NotBlank
        request.setAddress1("test");
        request.setAddress2("test");
        request.setAccountType(AccountType.S);

        mockMvc.perform(post("/api/v1/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.transactionStatusCode").value(400))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Email is required field"));
    }

    @Test
    public void testGetAccount_Success() throws Exception {
        CustomerInquiryResponse.AccountDto accountDto = CustomerInquiryResponse.AccountDto.builder()
                .accountNumber(10001L)
                .accountType("Savings")
                .availableBalance(new BigDecimal("500"))
                .build();

        CustomerInquiryResponse response = CustomerInquiryResponse.builder()
                .customerNumber(12345678L)
                .customerName("Test")
                .customerMobile("09081234567")
                .customerEmail("test12345@gmail.com")
                .address1("test")
                .address2("test")
                .savings(Collections.singletonList(accountDto))
                .transactionStatusCode(302)
                .transactionStatusDescription("Customer Account found")
                .build();

        when(accountService.getCustomerAccount(12345678L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/account/12345678"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.customerNumber").value(12345678))
                .andExpect(jsonPath("$.transactionStatusCode").value(302))
                .andExpect(jsonPath("$.savings[0].accountNumber").value(10001));
    }

    @Test
    public void testGetAccount_Failed_NotFound() throws Exception {
        when(accountService.getCustomerAccount(9999L)).thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(get("/api/v1/account/9999"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.transactionStatusCode").value(401))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Customer not found"));
    }
}
