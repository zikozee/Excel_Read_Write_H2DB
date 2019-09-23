package com.hbng.miniapp.charger.repository;

import com.hbng.miniapp.charger.model.ProcessedCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedCustomerRepo extends JpaRepository<ProcessedCustomer, Long> {
}
