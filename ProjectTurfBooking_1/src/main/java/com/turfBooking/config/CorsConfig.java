package com.turfBooking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Your React app URL

        // FIXED: Allow all headers including multipart form data headers
        config.setAllowedHeaders(Arrays.asList("*"));

        // FIXED: Expose these headers for file uploads
        config.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Content-Length",
                "Content-Disposition"
        ));

        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // FIXED: Increase max age for preflight caching
        config.setMaxAge(3600L); // Cache preflight for 1 hour

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}