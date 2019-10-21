package com.hbng.miniapp.charger.services;

import com.hbng.miniapp.charger.model.ProcessedCustomer;
import com.hbng.miniapp.charger.repository.ProcessedCustomerRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProcessedCustomerServiceImpl implements ProcessedCustomerService {

    private ProcessedCustomerRepo processedCustomerRepo;
    private CustomerService customerService;

    public ProcessedCustomerServiceImpl(ProcessedCustomerRepo processedCustomerRepo, CustomerService customerService) {
        this.processedCustomerRepo = processedCustomerRepo;
        this.customerService = customerService;
    }

    @Override
    public List<ProcessedCustomer> findAll() {
        return processedCustomerRepo.findAll();
    }

    @Override
    public void save(ProcessedCustomer processed) {
        processedCustomerRepo.save(processed);
    }

    @Override
    public void loadProcessedCustomer(double amountCharged, double limit) {
        Map<String, Integer> map = customerService.myMap(limit);

        long start = System.currentTimeMillis();
        /**
         * LONG PROCESS
         * */
//        for(String key: map.keySet()){
//            int value = map.get(key);
//            ProcessedCustomer processed = new ProcessedCustomer(key, value, amountCharged, (value*amountCharged));
//            save(processed);
//            log.info("<<<<<< COMPUTING CHARGES >>>>>");
//        }
        map.entrySet().parallelStream().forEach(entry ->{
            String key = entry.getKey();
            int value = entry.getValue();
            ProcessedCustomer processed = new ProcessedCustomer(key, value, amountCharged, (value*amountCharged));
            save(processed);
            log.info("<<<<<< COMPUTING CHARGES >>>>>");
        });
        long end  = System.currentTimeMillis();
        log.info("<<<<<<   ====>  COMPUTATION COMPLETED  <==== >>>>>");
        log.info(" Value in millis: " + (end - start));
    }
}
