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
        String traceId = request.getHeader("x-trace-id"); 
        String xForwardedFor = request.getHeader("x-forwarded-for");
        String remoteAddr = request.getRemoteAddr();

        // Check if the current RemoteAddr is an internal cluster/local IP
        boolean isInternal = remoteAddr.startsWith("10.") || 
                             remoteAddr.startsWith("172.") || 
                             remoteAddr.startsWith("192.168.") ||
                             remoteAddr.equalsIgnoreCase("localhost");

        // Logic: Trust Spring-processed remoteAddr if it's external. 
        // Otherwise, fall back to the raw header or the system address.
        String validatedIp;
        if (!isInternal) {
            validatedIp = remoteAddr;
        } else if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            validatedIp = xForwardedFor.split(",")[0].trim();
        } else {
            validatedIp = remoteAddr;
        }

        // PMD FIX: Removed extra parentheses around the ternary expression
        String displayTrace = traceId != null ? traceId : "N/A";
        
        System.out.printf("[Trace-%s] Backend Validation: Received=%s, Validated=%s%n", 
                          displayTrace, remoteAddr, validatedIp);

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
