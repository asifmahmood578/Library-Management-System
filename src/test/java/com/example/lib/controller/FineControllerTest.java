package com.example.lib.controller;

import com.example.lib.model.Fine;
import com.example.lib.service.FineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FineController.class)
class FineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FineService fineService;

    @Test
    void testGetFinesByCustomerId() throws Exception {
        Fine fine1 = new Fine();
        fine1.setId(1L);
        fine1.setAmount(25.0);
        fine1.setIssuedDate(LocalDate.now());

        Fine fine2 = new Fine();
        fine2.setId(2L);
        fine2.setAmount(50.0);
        fine2.setIssuedDate(LocalDate.now());

        when(fineService.getFinesByCustomerId(1L)).thenReturn(List.of(fine1, fine2));

        mockMvc.perform(get("/api/customers/1/fines")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(25.0))
                .andExpect(jsonPath("$[1].amount").value(50.0));
    }
}
