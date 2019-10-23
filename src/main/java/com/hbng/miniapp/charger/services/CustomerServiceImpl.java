package com.hbng.miniapp.charger.services;

import com.hbng.miniapp.charger.model.Customer;
import com.hbng.miniapp.charger.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> findByAmount(double limit) {
        //List<Customer> newList = new ArrayList<>();
        return findAll().parallelStream()
                .filter(Objects::nonNull)
                .filter(customer -> customer.getTransactedAmount() >= limit)
                .collect(Collectors.toList());

        //return newList;
    }

    @Override
    public Customer findById(Long theId) {
        Optional<Customer> result = customerRepository.findById(theId);

        Customer customer;
        if(result.isPresent()){
            customer = result.get();
        }else{
            // we didn't find the employee
            throw new RuntimeException("Did not find employee id - " + theId);
        }
        
        return customer;
    }

    @Override
    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void deleteById(Long theId) {
        customerRepository.deleteById(theId);
    }

    @Override
    public Map<String, Integer> myMap(double amount) {
        Map<String, Integer> map = new LinkedHashMap<>();

        findAll().stream()
                .filter(Objects::nonNull)
                .filter(customer -> customer.getTransactedAmount() >= amount)
                .parallel()
                .map(customer ->
                        (!map.containsKey(customer.getAccountNumber())
                                ? map.put(customer.getAccountNumber(), 1)
                                : map.put(customer.getAccountNumber(), map.get(customer.getAccountNumber()) +1)))
                .collect(Collectors.toList());

//        for(Customer customer: findAll()){
//            if(customer!=null && customer.getTransactedAmount() >= amount){
//                String accountNumber = customer.getAccountNumber();
//                if(!map.containsKey(accountNumber)){
//                    log.info("AM CHECKING HERE"+ customer.getTransactedAmount());
//                    map.put(accountNumber, 1);
//                }else{
//                    int value = map.get(accountNumber) +1 ;
//                    map.put(customer.getAccountNumber(), value);
//                }
//            }
//        }
        return map;
    }
}
