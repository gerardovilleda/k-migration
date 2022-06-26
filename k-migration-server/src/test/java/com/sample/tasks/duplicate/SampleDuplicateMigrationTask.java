package com.sample.tasks.duplicate;

import com.sample.tasks.ok.SampleMigrationTaskData;
import org.eventdrivendevelopment.kafka.migration.annotation.KMigrationTask;
import org.eventdrivendevelopment.kafka.migration.task.KMigrationTaskProcessor;

import java.util.Map;

/**
 * This is not a valid taskType as that task already exists.
 */
@KMigrationTask(taskType = "KMigrationCreateTopic", taskDataClass = SampleMigrationTaskData.class)
public class SampleDuplicateMigrationTask implements KMigrationTaskProcessor<SampleDuplicateMigrationTaskData> {

    @Override
    public void migrate(Map<String, Object> migrationContext, SampleDuplicateMigrationTaskData migrationData) {

    }

    @Override
    public void undo(Map<String, Object> migrationContext, SampleDuplicateMigrationTaskData migrationData) {

    }
}
