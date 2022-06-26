package com.sample.tasks.ok;

import org.eventdrivendevelopment.kafka.migration.model.KMigrationData;

public class SampleMigrationTaskData implements KMigrationData {

    @Override
    public String getTaskType() {
        return "SampleMigrationTask";
    }

}
