package org.eventdrivendevelopment.kafka.migration.server.task.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eventdrivendevelopment.kafka.migration.model.KMigrationData;

import java.util.Map;

public class KMigrationCreateTopicData implements KMigrationData {

    protected final String topicName;
    protected final Integer partitions;
    protected final Short replicationFactor;
    protected final Map<String, String> config;

    @Override
    public String getTaskType() {
        return "KMigrationCreateTopic";
    }

    @JsonCreator
    public KMigrationCreateTopicData(
            @JsonProperty(value = "topicName", required = true) final String topicName,
            @JsonProperty(value = "partitions") final Integer partitions,
            @JsonProperty(value = "replicationFactor") final Short replicationFactor,
            @JsonProperty(value = "config") final Map<String, String> config) {
        this.topicName = topicName;
        this.partitions = partitions;
        this.replicationFactor = replicationFactor;
        this.config = config;
    }

}
