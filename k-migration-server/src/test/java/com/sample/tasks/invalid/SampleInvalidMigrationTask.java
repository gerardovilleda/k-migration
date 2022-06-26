package com.sample.tasks.invalid;

import com.sample.tasks.ok.SampleMigrationTaskData;
import org.eventdrivendevelopment.kafka.migration.annotation.KMigrationTask;
import org.eventdrivendevelopment.kafka.migration.task.KMigrationTaskProcessor;

import java.util.Map;

@KMigrationTask(taskType = "SampleMigrationTask", taskDataClass = SampleMigrationTaskData.class)
public class SampleInvalidMigrationTask {

}
