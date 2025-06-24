package com.example.lib.controller;

import com.example.lib.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    private Map<String, Object> adminData;
    private Map<String, Object> customerData;

    @BeforeEach
    void setUp() {
        adminData = new HashMap<>();
        adminData.put("totalBooks", 100);
        adminData.put("borrowedBooks", 60);
        adminData.put("totalCustomers", 40);
        adminData.put("totalFines", 200.0);

        customerData = new HashMap<>();
        customerData.put("booksBorrowed", 3);
        customerData.put("finesOwed", 50.0);
    }

    @Test
    void testGetAdminDashboard() throws Exception {
        when(dashboardService.getAdminDashboard()).thenReturn(adminData);

        mockMvc.perform(get("/api/dashboard/admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalBooks").value(100))
                .andExpect(jsonPath("$.borrowedBooks").value(60))
                .andExpect(jsonPath("$.totalCustomers").value(40))
                .andExpect(jsonPath("$.totalFines").value(200.0));
    }

    @Test
    void testGetCustomerDashboard() throws Exception {
        when(dashboardService.getCustomerDashboard(1L)).thenReturn(customerData);

        mockMvc.perform(get("/api/dashboard/customer/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booksBorrowed").value(3))
                .andExpect(jsonPath("$.finesOwed").value(50.0));
    }
}
