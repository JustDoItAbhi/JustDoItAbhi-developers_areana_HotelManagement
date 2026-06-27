package com.locationservice.locationservice.locations.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBean {
    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }
}
