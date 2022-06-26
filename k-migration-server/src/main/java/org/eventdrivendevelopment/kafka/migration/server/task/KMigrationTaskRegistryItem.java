package org.eventdrivendevelopment.kafka.migration.server.task;

import org.eventdrivendevelopment.kafka.migration.task.KMigrationTaskProcessor;

public class KMigrationTaskRegistryItem {

    private final KMigrationTaskProcessor<?> taskProcessor;
    private final Class taskDataClass;

    public KMigrationTaskRegistryItem(final KMigrationTaskProcessor<?> taskProcessor, final Class taskDataClass) {
        this.taskProcessor = taskProcessor;
        this.taskDataClass = taskDataClass;
    }

    public KMigrationTaskProcessor<?> getTaskProcessor() {
        return taskProcessor;
    }

    public Class getTaskDataClass() {
        return taskDataClass;
    }

}
