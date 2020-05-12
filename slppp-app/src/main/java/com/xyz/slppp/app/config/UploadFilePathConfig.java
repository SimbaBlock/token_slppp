package com.xyz.slppp.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UploadFilePathConfig  implements WebMvcConfigurer {

    private String staticAccessPath = "/upload-images/**";
    private String uploadFolder = "/java/upay/upload-images/";

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(staticAccessPath).addResourceLocations("file://" + uploadFolder);
    }
}
