package com.pluralsight.conferencedemo;

import com.pluralsight.conferencedemo.filters.LoggingFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YAMLConfig {

    private String name;
    private String environment;
    private boolean enabled;
    private List<String> servers = new ArrayList<>();

    // standard getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilter(){
        System.out.println("Registering Logging Filter");
        FilterRegistrationBean<LoggingFilter> logFilter = new FilterRegistrationBean<LoggingFilter>();
        logFilter.setFilter(new LoggingFilter());
        logFilter.addUrlPatterns("/*");
        return logFilter;
    }

    @PreDestroy
    public void OnShutdown(){
        System.out.println("SpringApplication is exiting.");
        try{
            ConferenceDemoApplication.getRedisListener().Unsubscribe();
            ConferenceDemoApplication.getListenerThread().join(500);
            System.out.println("listener thread has stopped.");
        }
        catch(InterruptedException ex){
            System.out.println(ex.getMessage());
        }
    }
}
