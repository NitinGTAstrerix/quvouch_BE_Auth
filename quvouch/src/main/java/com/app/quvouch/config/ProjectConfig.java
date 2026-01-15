package com.app.quvouch.config;

import com.app.quvouch.Models.Business;
import com.app.quvouch.dtos.BusinessResponse;
import com.app.quvouch.dtos.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }


}