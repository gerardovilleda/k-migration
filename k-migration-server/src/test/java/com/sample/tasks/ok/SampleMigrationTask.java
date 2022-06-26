package com.sample.tasks.ok;

import org.eventdrivendevelopment.kafka.migration.annotation.KMigrationTask;
import org.eventdrivendevelopment.kafka.migration.task.KMigrationTaskProcessor;

import java.util.Map;

@KMigrationTask(taskType = "SampleMigrationTask", taskDataClass = SampleMigrationTaskData.class)
public class SampleMigrationTask implements KMigrationTaskProcessor<SampleMigrationTaskData> {

    @Override
    public void migrate(Map<String, Object> migrationContext, SampleMigrationTaskData migrationData) {

    }

    @Override
    public void undo(Map<String, Object> migrationContext, SampleMigrationTaskData migrationData) {

    }
}
