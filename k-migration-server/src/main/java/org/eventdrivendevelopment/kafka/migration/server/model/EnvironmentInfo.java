package org.eventdrivendevelopment.kafka.migration.server.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class EnvironmentInfo {

    private final String environment;
    private final String prefix;
    private final String description;
    private final boolean approvalsRequired;
    private final Map<String, Object> properties;

    @JsonCreator
    public EnvironmentInfo(
            @JsonProperty(value = "environment") final String environment,
            @JsonProperty(value = "prefix") final String prefix,
            @JsonProperty(value = "description") final String description,
            @JsonProperty(value = "approvalsRequired") final boolean approvalsRequired,
            @JsonProperty(value = "properties") final Map<String, Object> properties) {
        this.prefix = prefix;
        this.environment = environment;
        this.description = description;
        this.approvalsRequired = approvalsRequired;
        this.properties = properties;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getDescription() {
        return description;
    }

    public boolean isApprovalsRequired() {
        return approvalsRequired;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "EnvironmentInfo{" +
                "environment='" + environment + '\'' +
                ", prefix='" + prefix + '\'' +
                ", description='" + description + '\'' +
                ", approvalsRequired=" + approvalsRequired +
                ", properties=" + properties +
                '}';
    }
}
