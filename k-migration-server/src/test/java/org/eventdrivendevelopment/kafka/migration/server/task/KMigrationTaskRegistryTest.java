package org.eventdrivendevelopment.kafka.migration.server.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eventdrivendevelopment.kafka.migration.server.exception.KMigrationServerException;
import org.eventdrivendevelopment.kafka.migration.server.model.KMigration;
import org.eventdrivendevelopment.kafka.migration.server.task.topic.KMigrationCreateTopicData;
import org.eventdrivendevelopment.kafka.migration.server.util.TestUtils;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class KMigrationTaskRegistryTest {

    List<String> prefixes = Arrays.asList("com.sample.tasks.ok");
    List<String> prefixesDuplicate = Arrays.asList("com.sample.tasks.duplicate");
    List<String> prefixesInvalid = Arrays.asList("com.sample.tasks.invalid");

    @Test
    public void testKMigrationTaskRegistry() {
        KMigrationTaskRegistry migrationTaskRegistry = new KMigrationTaskRegistry(prefixes);
        assertTrue(migrationTaskRegistry.getRegistry().containsKey("KMigrationCreateTopic"));
        KMigrationTaskRegistryItem createTopicTask = migrationTaskRegistry.getRegistry().get("KMigrationCreateTopic");
        assertEquals(KMigrationCreateTopicData.class, createTopicTask.getTaskDataClass());
    }

    @Test
    public void testParseMigrationFile() throws Exception {
        KMigrationTaskRegistry migrationTaskRegistry = new KMigrationTaskRegistry(prefixes);
        String samplePayload = TestUtils.testResourceFileText("KMigrationCreateTopic.json");
        KMigration migration = migrationTaskRegistry.parseMigration(samplePayload);
        assertNotNull(migration);
        assertEquals("k-migration.test", migration.getApplicationId());
        assertEquals("gerardo", migration.getAuthor());
        assertEquals("some test", migration.getDocumentation());
        assertTrue(migration.toString().contains("KMigration{"));
        assertTrue(migration.getData() instanceof KMigrationCreateTopicData);
        assertEquals("KMigrationCreateTopic", migration.getData().getTaskType());
    }

    @Test(expected = KMigrationServerException.class)
    public void testKMigrationTaskRegistryDuplicateTask() {
        KMigrationTaskRegistry migrationTaskRegistry = new KMigrationTaskRegistry(prefixesDuplicate);
    }

    @Test(expected = KMigrationServerException.class)
    public void testKMigrationTaskRegistryInvalidTask() {
        KMigrationTaskRegistry migrationTaskRegistry = new KMigrationTaskRegistry(prefixesInvalid);
    }

    @Test
    public void testMigrate() throws Exception {
        KMigrationTaskRegistry migrationTaskRegistry = new KMigrationTaskRegistry(prefixes);
        String samplePayload = TestUtils.testResourceFileText("KMigrationCreateTopic.json");
        migrationTaskRegistry.migrate(samplePayload);
    }

    @Test(expected = KMigrationServerException.class)
    public void testParseInvalidType() throws Exception {
        KMigrationTaskRegistry migrationTaskRegistry = new KMigrationTaskRegistry(prefixes);
        String samplePayload = TestUtils.testResourceFileText("InvalidMigrationType.json");
        KMigration migration = migrationTaskRegistry.parseMigration(samplePayload);
    }

    @Test
    public void testMapToJson() throws Exception {
        List<Map<String, Object>> migrations = new ArrayList<>();
        Map<String, Object> item1 = new HashMap<>();
        item1.put("name", "Gerardo");
        item1.put("number", 1L);
        migrations.add(item1);
        Map<String, Object> item2 = new HashMap<>();
        item2.put("name", "Carmen");
        item2.put("timestamp", System.currentTimeMillis());
        migrations.add(item2);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(migrations);
        System.out.println(json);
    }

}