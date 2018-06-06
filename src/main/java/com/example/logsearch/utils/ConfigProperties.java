package com.example.logsearch.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "logs")
@PropertySource("classpath:props.properties")
@EnableConfigurationProperties(value = ConfigProperties.class)
@Getter
@Setter
public class ConfigProperties {

    @Value("${logs.path}")
    private String path;

    @Value("${logs.fileLink}")
    private String fileLink;

    @Value("${logs.deletionInterval}")
    private int deletionInterval;

    @Value("${logs.fileExistTime}")
    private int fileExistTime;

    @Value("${logs.threadsNumber}")
    private int threadsNumber;
}
