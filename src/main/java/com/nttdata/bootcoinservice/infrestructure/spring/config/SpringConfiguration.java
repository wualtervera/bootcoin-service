package com.nttdata.bootcoinservice.infrestructure.spring.config;

import com.nttdata.bootcoinservice.application.model.BootcoinRepository;
import com.nttdata.bootcoinservice.infrestructure.rest.service.BootcoinCrudService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {

    @Bean
    public BootcoinRepository walletRepository(){
        return new BootcoinCrudService();
    }
}
