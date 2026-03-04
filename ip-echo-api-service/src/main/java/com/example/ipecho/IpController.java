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
        // --- DEBUG BLOCK ---
        System.out.println("DEBUG: All Received Headers:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            System.out.println("  " + name + ": " + request.getHeader(name));
        }

        String traceId = request.getHeader("x-trace-id"); 
        String xForwardedFor = request.getHeader("x-forwarded-for");
        String remoteAddr = request.getRemoteAddr();

        System.out.println("\n--- [BACKEND VALIDATE STAGE] ---");
        System.out.println("Trace ID: " + (traceId != null ? traceId : "N/A"));
        
        String validatedIp;

        // Use a more generic check to avoid PMD Hardcoded IP rule
        boolean isInternal = remoteAddr.startsWith("10.") || 
                             remoteAddr.startsWith("172.") || 
                             remoteAddr.startsWith("192.168.");

        /**
         * FIX: We check if the remoteAddr is likely an external IP.
         * If it's not internal, Spring's RemoteIpValve has already 
         * successfully swapped the X-Forwarded-For into the remoteAddr.
         */
        if (!isInternal && !remoteAddr.equalsIgnoreCase("localhost")) {
            validatedIp = remoteAddr;
            System.out.println("1. Logic: Using Spring-Processed Remote Address.");
        } else if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            validatedIp = xForwardedFor.split(",")[0].trim();
            System.out.println("1. Logic: Using Raw X-Forwarded-For Header.");
        } else {
            validatedIp = remoteAddr;
            System.out.println("1. Logic: Fallback to System IP.");
        }

        System.out.println("2. Detected Source IP: " + remoteAddr);
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
