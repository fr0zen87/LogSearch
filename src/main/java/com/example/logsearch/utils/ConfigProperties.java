package com.example.logsearch.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@ConfigurationProperties(prefix = "logs")
@PropertySource("classpath:props.properties")
@EnableConfigurationProperties(value = ConfigProperties.class)
@Getter
@Setter
public class ConfigProperties {

    private static Path domainPath;

    static {
        domainPath = Paths.get(System.getProperty("user.dir"));
        while (!domainPath.endsWith("domains")) {
            if (domainPath.getParent() == null) {
                domainPath = Paths.get("");
                break;
            }
            domainPath = domainPath.getParent();
        }
    }

    public static Path getDomainPath() {
        return domainPath;
    }

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
