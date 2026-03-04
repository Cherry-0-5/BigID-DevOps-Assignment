package com.example.ipecho;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class IpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET / should return the remote IP address")
    void shouldReturnRemoteIp() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ip").exists());
    }

    @Test
    @DisplayName("GET / should respect X-Forwarded-For header")
    void shouldReturnForwardedIp() throws Exception {
        String testIp = "1.2.3.4";
        mockMvc.perform(get("/").header("X-Forwarded-For", testIp))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ip").value(testIp));
    }

    // --- NEW TESTS ADDED BELOW TO INCREASE COVERAGE ---

    @Test
    @DisplayName("GET / should handle empty X-Forwarded-For header gracefully")
    void shouldHandleEmptyForwardedHeader() throws Exception {
        mockMvc.perform(get("/").header("X-Forwarded-For", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ip").exists());
    }

    @Test
    @DisplayName("GET /invalid-path should return 404 Not Found")
    void shouldReturnNotFoundForInvalidPath() throws Exception {
        mockMvc.perform(get("/invalid-path"))
                .andExpect(status().isNotFound());
    }
    
    // --- END OF NEW TESTS ---

    @Test
    @DisplayName("GET /health should return UP status for Liveness probes")
    void shouldReturnLivenessStatus() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    @DisplayName("GET /ready should return READY status for Readiness probes")
    void shouldReturnReadinessStatus() throws Exception {
        mockMvc.perform(get("/ready"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("READY"));
    }
}
