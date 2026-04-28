package com.conferencehub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * AppConfig — configures static resource serving for uploaded files.
 * Uploaded files are accessible at /api/files/{filename} for direct URL access.
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Expose the uploads folder as a static resource directory.
     * This is a convenience for direct file previews; the preferred method
     * is the authenticated /api/papers/{id}/download endpoint.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = Paths.get(uploadDir).toAbsolutePath().normalize().toString();
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + absolutePath + "/");
    }
}
