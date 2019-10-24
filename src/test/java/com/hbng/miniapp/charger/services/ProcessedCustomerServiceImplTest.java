package com.hbng.miniapp.charger.services;

import com.hbng.miniapp.charger.model.ProcessedCustomer;
import com.hbng.miniapp.charger.repository.ProcessedCustomerRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessedCustomerServiceImplTest {

    @Mock
    ProcessedCustomerRepo processedCustomerRepo;

    @Mock
    CustomerService customerService;

    @InjectMocks
    ProcessedCustomerServiceImpl processedCustomerService;

    List<ProcessedCustomer> processedCustomers;

    @BeforeEach
    void setUp() {
        processedCustomers = new ArrayList<>();
        processedCustomers.add(new ProcessedCustomer("a", 5, 6.0, 10.0));
        processedCustomers.add(new ProcessedCustomer("b", 2, 2.0, 3.0));
    }

    @Test
    void findAll() {
        when(processedCustomerRepo.findAll()).thenReturn(processedCustomers);
        assertEquals(2, processedCustomerRepo.findAll().size());
        verify(processedCustomerRepo, times(1)).findAll();

    }

    @Test
    void save() {
        processedCustomers.add(new ProcessedCustomer("c", 5, 6.0, 10.0));

        when(processedCustomerRepo.findAll()).thenReturn(processedCustomers);

        assertEquals(3, processedCustomerRepo.findAll().size());
        verify(processedCustomerRepo, times(1)).findAll();
    }

    @Test
    void loadProcessedCustomer() {
//        Map<String,Integer> map = new LinkedHashMap<>();
//        map.put("key", 1);
//        when(customerService.myMap(2.0)).thenReturn(map);

        verifyZeroInteractions(customerService);
        verifyZeroInteractions(processedCustomerRepo);
    }
}