package com.mbt.exam.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerNumber;

    @Column(length = 50, nullable = false)
    private String customerName;

    @Column(length = 20, nullable = false)
    private String customerMobile;

    @Column(length = 50, nullable = false)
    private String customerEmail;

    @Column(length = 100, nullable = false)
    private String address1;

    @Column(length = 100)
    private String address2;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Account> savings = new ArrayList<>();
    
    public void addAccount(Account account) {
        savings.add(account);
        account.setCustomer(this);
    }
}
