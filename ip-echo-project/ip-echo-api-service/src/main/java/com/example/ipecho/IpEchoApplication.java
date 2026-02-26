package com.example.ipecho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The standard entry point for the IP-Echo Spring Boot application.
 * This class bootstraps the application, starting the embedded Tomcat server on port 8088.
 */
@SpringBootApplication
public class IpEchoApplication {

    /**
     * Main method that launches the application.
     * * @param args Command line arguments passed to the application.
     */
    public static void main(final String[] args) {
        SpringApplication.run(IpEchoApplication.class, args);
    }
}
