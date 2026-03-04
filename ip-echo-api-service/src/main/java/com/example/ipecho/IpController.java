package com.example.ipecho;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IpController {

    @GetMapping("/")
    public Map<String, String> getClientIp(HttpServletRequest request) {
        String traceId = request.getHeader("X-Trace-ID");
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        String systemRemoteIp = request.getRemoteAddr();

        System.out.println("\n--- [BACKEND VALIDATE STAGE] ---");
        System.out.println("Trace ID: " + (traceId != null ? traceId : "N/A"));
        System.out.println("1. System (Linux Kernel) sees Source IP: " + systemRemoteIp);
        
        String validatedIp;
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            validatedIp = xForwardedFor.split(",")[0].trim();
            System.out.println("2. Validation Logic: X-Forwarded-For detected!");
            System.out.println("3. Extracted Client IP: " + validatedIp);
        } else {
            validatedIp = systemRemoteIp;
            System.out.println("2. Validation Logic: No headers found. Using System IP.");
        }
        System.out.println("--------------------------------\n");

        return Collections.singletonMap("ip", validatedIp);
    }

    @GetMapping("/health")
    public Map<String, String> liveness() {
        return Collections.singletonMap("status", "UP");
    }

    @GetMapping("/ready")
    public Map<String, String> readiness() {
        return Collections.singletonMap("status", "READY");
    }
}
