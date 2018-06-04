package com.example.logsearch.utils;

import lombok.Getter;
import lombok.Setter;
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

    private String path;

    private String fileLink;

    private int deletionInterval;

    private int fileExistTime;

    private int threadsNumber;
}
