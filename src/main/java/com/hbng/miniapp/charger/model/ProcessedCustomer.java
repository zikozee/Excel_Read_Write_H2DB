package com.hbng.miniapp.charger.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class ProcessedCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private int transactedTimes;
    private double chargedAmount;
    private double payable;

    public ProcessedCustomer(String accountNumber, int transactedTimes, double chargedAmount, double payable) {
        this.accountNumber = accountNumber;
        this.transactedTimes = transactedTimes;
        this.chargedAmount = chargedAmount;
        this.payable = payable;
    }
}