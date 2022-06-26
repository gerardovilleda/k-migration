package com.sample.tasks.duplicate;

import org.eventdrivendevelopment.kafka.migration.model.KMigrationData;

public class SampleDuplicateMigrationTaskData implements KMigrationData {

    @Override
    public String getTaskType() {
        return "SampleDuplicateMigrationTask";
    }

}
