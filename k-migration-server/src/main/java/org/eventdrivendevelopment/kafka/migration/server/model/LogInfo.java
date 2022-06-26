package org.eventdrivendevelopment.kafka.migration.server.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class LogInfo {

    private final int partition;
    private final long offset;
    private final long timestamp;
    private final String applicationId;
    private final Map<String, String> headers;
    private final String text;

    @JsonCreator
    public LogInfo(
            @JsonProperty("partition") final int partition,
            @JsonProperty("offset") final long offset,
            @JsonProperty("timestamp") final long timestamp,
            @JsonProperty("applicationId") final String applicationId,
            @JsonProperty("headers") final Map<String, String> headers,
            @JsonProperty("text") final String text) {
        this.partition = partition;
        this.offset = offset;
        this.timestamp = timestamp;
        this.applicationId = applicationId;
        this.headers = headers;
        this.text = text;
    }

    public int getPartition() {
        return partition;
    }

    public long getOffset() {
        return offset;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "LogInfo{" +
                "partition=" + partition +
                ", offset=" + offset +
                ", timestamp=" + timestamp +
                ", applicationId='" + applicationId + '\'' +
                ", headers=" + headers +
                ", text='" + text + '\'' +
                '}';
    }
}
