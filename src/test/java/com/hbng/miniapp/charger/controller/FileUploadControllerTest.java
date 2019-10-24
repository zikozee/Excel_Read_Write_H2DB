package com.hbng.miniapp.charger.controller;

import com.hbng.miniapp.charger.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class FileUploadControllerTest {

    @Mock
    StorageService storageService;

    @InjectMocks
    FileUploadController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc= MockMvcBuilders
                .standaloneSetup(controller).build();
    }

    @Test
    void listUploadedFiles() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(model().attributeExists("files"))
                .andExpect(view().name("uploadForm"));

        verify(storageService, times(1)).loadAll();
    }

    @Test
    void serveFile() throws Exception {

    }

    @Test
    void handleFileUpload() throws Exception {
        //MockMultipartFile secondFile = new MockMultipartFile("data", "other-file-name.data", "text/plain", "some other type".getBytes());
    }

}