package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Enable CORS (Cross-Origin Resource Sharing)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Allow all endpoints
                .allowedOrigins("http://localhost:3000")  // Allow only the frontend running on port 3000
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Allow these HTTP methods
                .allowedHeaders("*")  // Allow all headers
                .allowCredentials(true);  // Allow credentials like cookies
    }

    // Configure resource handlers to serve static files (e.g., uploaded files like videos)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Adjusted to serve uploaded files from the project's 'uploads' directory
        registry.addResourceHandler("/uploads/**")  // The URL pattern for accessing files
                .addResourceLocations("file:src/main/resources/static/uploads");  // The directory where the files are stored
    }
}
