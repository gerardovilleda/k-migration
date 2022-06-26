package org.eventdrivendevelopment.kafka.migration.task;

import org.eventdrivendevelopment.kafka.migration.model.KMigrationData;

import java.util.Map;

public interface KMigrationTaskProcessor<T extends KMigrationData> {

    /**
     * Run the migration associated with the data provided.
     * @param migrationData - migration data
     */
    void migrate(final Map<String, Object> migrationContext, final T migrationData);

    /**
     * If possible, undo the changes associated with this migration
     * @param migrationData - migration data
     */
    void undo(final Map<String, Object> migrationContext, final T migrationData);
}
