package org.example.migration;

import org.eventdrivendevelopment.kafka.migration.annotation.KMigrationTask;
import org.eventdrivendevelopment.kafka.migration.task.KMigrationTaskProcessor;

@KMigrationTask(type = "SampleMigration", taskDataClass = SampleMigrationData.class)
public class SampleMigration implements KMigrationTaskProcessor<SampleMigrationData> {

    @Override
    public void migrate(SampleMigrationData migrationData) {

    }

    @Override
    public void undo(SampleMigrationData migrationData) {

    }
}
