package com.hbng.miniapp.charger.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private BigDecimal transactedAmount;

    public Customer(String accountNumber, BigDecimal transactedAmount) {
        this.accountNumber = accountNumber;
        this.transactedAmount = transactedAmount;
    }
}
