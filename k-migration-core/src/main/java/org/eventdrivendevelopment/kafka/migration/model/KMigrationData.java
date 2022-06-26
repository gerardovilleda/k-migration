package org.eventdrivendevelopment.kafka.migration.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "taskType")
public interface KMigrationData {

    String getTaskType();

}
