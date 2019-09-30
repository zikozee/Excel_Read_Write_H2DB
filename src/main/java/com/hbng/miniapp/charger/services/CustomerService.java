package com.hbng.miniapp.charger.services;

import com.hbng.miniapp.charger.model.Customer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CustomerService {

     List<Customer> findAll();

     List<Customer> findByAmount(BigDecimal amount);
     Customer findById(Long theId);

     void save(Customer customer);

     void deleteById(Long theId);

     Map<String, Integer> myMap(BigDecimal amount);
}
