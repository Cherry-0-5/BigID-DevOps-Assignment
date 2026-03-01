package com.example.ipecho;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller providing IP echo functionality and specialized Kubernetes probes.
 * Built for port 8088 with custom health and readiness paths.
 */
@RestController
public class IpController {

    /**
     * Primary endpoint: Returns the client's originating IP address.
     * Handles X-Forwarded-For headers for apps behind Ingress/Load Balancers.
     *
     * @param request The incoming HTTP request.
     * @return A Map containing the "ip" key and the string IP address.
     */
    @GetMapping("/")
    public Map<String, String> getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        String remoteIp;

//	String podIp = System.getenv("POD_IP");
	
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            remoteIp = xForwardedFor.split(",")[0].trim();
        } else {
            remoteIp = request.getRemoteAddr();
        }

        return Collections.singletonMap("ip", remoteIp);
    }

    /**
     * Liveness Probe: Used by Kubernetes to check if the JVM is alive.
     * Path: /health
     */
    @GetMapping("/health")
    public Map<String, String> liveness() {
        return Collections.singletonMap("status", "UP");
    }

    /**
     * Readiness Probe: Used by Kubernetes to check if the app is ready for traffic.
     * Path: /ready
     */
    @GetMapping("/ready")
    public Map<String, String> readiness() {
        // In a production scenario, you could add logic here to check
        // if critical downstream dependencies are reachable.
        return Collections.singletonMap("status", "READY");
    }
}
