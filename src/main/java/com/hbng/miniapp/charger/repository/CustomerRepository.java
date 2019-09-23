package com.hbng.miniapp.charger.repository;

import com.hbng.miniapp.charger.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
