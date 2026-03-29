package com.mbt.exam.dto;

import com.mbt.exam.model.AccountType;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AccountCreationRequest {

    @NotBlank(message = "customerName is required")
    @Size(max = 50)
    private String customerName;

    @NotBlank(message = "customerMobile is required")
    @Size(max = 20)
    private String customerMobile;

    @NotBlank(message = "Email is required field")
    @Email(message = "Email must be valid")
    @Size(max = 50)
    private String customerEmail;

    @NotBlank(message = "address1 is required")
    @Size(max = 100)
    private String address1;

    @Size(max = 100)
    private String address2;

    @NotNull(message = "accountType is required")
    private AccountType accountType;
}
