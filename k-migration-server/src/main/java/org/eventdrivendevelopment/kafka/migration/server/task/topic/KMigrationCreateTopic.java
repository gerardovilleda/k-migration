package org.eventdrivendevelopment.kafka.migration.server.task.topic;

import org.eventdrivendevelopment.kafka.migration.annotation.KMigrationTask;
import org.eventdrivendevelopment.kafka.migration.task.KMigrationTaskProcessor;

import java.util.Map;

@KMigrationTask(taskType = "KMigrationCreateTopic", taskDataClass = KMigrationCreateTopicData.class)
public class KMigrationCreateTopic implements KMigrationTaskProcessor<KMigrationCreateTopicData> {

    @Override
    public void migrate(final Map<String, Object> migrationContext, final KMigrationCreateTopicData migrationData) {
        System.out.println("");
    }

    @Override
    public void undo(final Map<String, Object> migrationContext, final KMigrationCreateTopicData migrationData) {

    }
}
