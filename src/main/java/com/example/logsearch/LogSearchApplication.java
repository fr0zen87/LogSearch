package com.example.logsearch;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ConfigurationBuilderEvent;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.WebApplicationInitializer;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class LogSearchApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    public static void main(String[] args) {
        SpringApplication.run(LogSearchApplication.class, args);
    }

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.initialize();
        return executor;
    }

    @Bean
    public ReloadingFileBasedConfigurationBuilder builder() {
        Parameters parameters = new Parameters();
        File properties = new File("props.properties");
        ReloadingFileBasedConfigurationBuilder builder =
                new ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(parameters.fileBased()
                                .setFile(properties));

        PeriodicReloadingTrigger trigger = new PeriodicReloadingTrigger(
                builder.getReloadingController(), null, 5, TimeUnit.SECONDS);
        trigger.start();

        builder.addEventListener(ConfigurationBuilderEvent.CONFIGURATION_REQUEST,
                event -> builder.getReloadingController().checkForReloading(null));

        return builder;
    }
}
