package com.hbng.miniapp.charger.controller;

import com.hbng.miniapp.charger.model.Customer;
import com.hbng.miniapp.charger.model.ProcessedCustomer;
import com.hbng.miniapp.charger.services.ExcelUtil;
import com.hbng.miniapp.charger.services.ProcessedCustomerService;
import com.hbng.miniapp.charger.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlTemplate;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ExcelControllerTest {
    @Mock
    StorageService storageService;

    @Mock
    ProcessedCustomerService processedCustomerService;
    @Mock
    ExcelUtil excelUtil;

    @InjectMocks
    ExcelController controller;


    List<ProcessedCustomer> processedCustomers;

    Set<Customer> customers;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {


        customers = new HashSet<>();
        customers.add(new Customer("customer1", 5.0));
        customers.add(new Customer("customer2", 6.0));

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller).build();
    }

    @Test
    void readExcel() throws Exception {
        when(storageService.getFilePath()).thenReturn("");

        mockMvc.perform(post("/readExcel"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/"));

        verify(storageService, times(2)).getFilePath();
        verify(excelUtil, times(1)).loadExcel(storageService.getFilePath());
    }

    @Test
    void computeValue() throws Exception {
        mockMvc.perform(post("/computeValue"))
                .andExpect(status().isBadRequest());


        verifyZeroInteractions(processedCustomerService);

    }

    @Test
    void write() throws Exception {
        when(storageService.getDestinationPath()).thenReturn("");
        mockMvc.perform(post("/writeExcel"))
                .andExpect(status().is3xxRedirection());

        verify(storageService, times(1)).getDestinationPath();
        verify(excelUtil, times(1)).writeExcel(storageService.getDestinationPath());
    }
}