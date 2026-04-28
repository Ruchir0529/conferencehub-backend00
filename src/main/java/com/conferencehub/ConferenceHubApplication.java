package com.conferencehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Conference Hub - Academic Conference Management System
 * Main application entry point.
 */
@SpringBootApplication
@EnableConfigurationProperties
public class ConferenceHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConferenceHubApplication.class, args);
        System.out.println("""
                ================================================
                  Conference Hub is running!
                  API Base URL: http://localhost:8080/api
                ================================================
                """);
    }
}
