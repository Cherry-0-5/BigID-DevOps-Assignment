package com.example.ipecho;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.Enumeration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IpController {

    @GetMapping("/")
    public Map<String, String> getClientIp(HttpServletRequest request) {
        // --- DEBUG BLOCK: SEE EVERYTHING ARRIVING ---
        System.out.println("DEBUG: All Received Headers:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            System.out.println("  " + name + ": " + request.getHeader(name));
        }
        // --------------------------------------------

        // Use lowercase strings just in case a proxy flattened the case
        String traceId = request.getHeader("x-trace-id"); 
        String xForwardedFor = request.getHeader("x-forwarded-for");
        String systemRemoteIp = request.getRemoteAddr();

        System.out.println("\n--- [BACKEND VALIDATE STAGE] ---");
        System.out.println("Trace ID: " + (traceId != null ? traceId : "N/A"));
        System.out.println("1. System (Linux Kernel) sees Source IP: " + systemRemoteIp);
        
        String validatedIp;
        // Check both the raw header and the processed remote address
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            validatedIp = xForwardedFor.split(",")[0].trim();
            System.out.println("2. Validation Logic: X-Forwarded-For header detected!");
        } else if (!systemRemoteIp.startsWith("10.") && !systemRemoteIp.startsWith("172.")) {
            // If Spring already processed the header, it moved the real IP into getRemoteAddr()
            validatedIp = systemRemoteIp;
            System.out.println("2. Validation Logic: Header was processed by Spring. Using System IP.");
        } else {
            validatedIp = systemRemoteIp;
            System.out.println("2. Validation Logic: No external headers found. Using System IP.");
        }
        
        System.out.println("3. Final Validated IP: " + validatedIp);
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
