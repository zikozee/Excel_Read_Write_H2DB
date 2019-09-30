package com.hbng.miniapp.charger.services;

import com.hbng.miniapp.charger.model.ProcessedCustomer;

import java.math.BigDecimal;
import java.util.List;

public interface ProcessedCustomerService {
     List<ProcessedCustomer> findAll();
     void save(ProcessedCustomer processed);
     void loadProcessedCustomer(BigDecimal amountCharged, BigDecimal limit);
}
