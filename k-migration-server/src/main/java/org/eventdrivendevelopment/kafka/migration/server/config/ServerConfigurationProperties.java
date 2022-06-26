package org.eventdrivendevelopment.kafka.migration.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "kmigration")
public class ServerConfigurationProperties {

    /**
     * The default environment will be used when no environment is provided
     */
    private String defaultEnvironmentPrefix;

    private List<String> customTaskSearchPrefixes;

    private List<String> bootstrapServers;

    private Map<String, String> producer = new HashMap<>();

    private Map<String, String> consumer = new HashMap<>();

    private Map<String, String> admin = new HashMap<>();

    private Map<String, String> stream = new HashMap<>();

    public String getDefaultEnvironmentPrefix() {
        return defaultEnvironmentPrefix;
    }

    public void setDefaultEnvironmentPrefix(String defaultEnvironmentPrefix) {
        this.defaultEnvironmentPrefix = defaultEnvironmentPrefix;
    }

    public List<String> getCustomTaskSearchPrefixes() {
        return customTaskSearchPrefixes;
    }

    public void setCustomTaskSearchPrefixes(List<String> customTaskSearchPrefixes) {
        this.customTaskSearchPrefixes = customTaskSearchPrefixes;
    }

    public List<String> getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(List<String> bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public Map<String, String> getProducer() {
        return producer;
    }

    public void setProducer(Map<String, String> producer) {
        this.producer = producer;
    }

    public Map<String, String> getConsumer() {
        return consumer;
    }

    public void setConsumer(Map<String, String> consumer) {
        this.consumer = consumer;
    }

    public Map<String, String> getAdmin() {
        return admin;
    }

    public void setAdmin(Map<String, String> admin) {
        this.admin = admin;
    }

    public Map<String, String> getStream() {
        return stream;
    }

    public void setStream(Map<String, String> stream) {
        this.stream = stream;
    }
}
