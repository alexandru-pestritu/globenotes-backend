package com.app.globenotes_backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
@Slf4j
public class AwsConfig {

    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}