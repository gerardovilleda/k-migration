package org.eventdrivendevelopment.kafka.migration.server.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eventdrivendevelopment.kafka.migration.model.KMigrationData;

public class KMigration {

    private final String applicationId;

    private final String author;

    private final String documentation;

    private final KMigrationData data;

    @JsonCreator
    public KMigration(
            @JsonProperty("applicationId") final String applicationId,
            @JsonProperty("author") final String author,
            @JsonProperty("documentation") final String documentation,
            @JsonProperty("data") final KMigrationData data) {
        this.applicationId = applicationId;
        this.author = author;
        this.documentation = documentation;
        this.data = data;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getAuthor() {
        return author;
    }

    public String getDocumentation() {
        return documentation;
    }

    public KMigrationData getData() {
        return data;
    }

    @Override
    public String toString() {
        return "KMigration{" +
                "applicationId='" + applicationId + '\'' +
                ", author='" + author + '\'' +
                ", documentation='" + documentation + '\'' +
                ", data=" + data +
                '}';
    }
}
