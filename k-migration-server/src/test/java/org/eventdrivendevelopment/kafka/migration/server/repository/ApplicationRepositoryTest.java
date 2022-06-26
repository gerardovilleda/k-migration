package org.eventdrivendevelopment.kafka.migration.server.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.producer.MockProducer;
import org.eventdrivendevelopment.kafka.migration.server.exception.KMigrationServerException;
import org.eventdrivendevelopment.kafka.migration.server.model.ApplicationInfo;
import org.eventdrivendevelopment.kafka.migration.server.util.TestUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApplicationRepositoryTest {

    private MockProducer<String, String> producer;
    private MockConsumer<String, String> consumer;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testAddAndRetrieve() throws Exception {
        ApplicationRepository repository = new ApplicationRepository(objectMapper, producer, consumer);
        String applicationInfo = TestUtils.testResourceFileText("ApplicationInfo.json");
        repository.addToCache("sample-id", applicationInfo);
        assertEquals(1, repository.all().size());
        ApplicationInfo app = repository.all().get(0);
        assertEquals("sample-application-0001", app.getId());
        assertEquals("This is the description", app.getDescription());
        assertEquals("owner@email.com", app.getOwner());
        assertFalse(app.getProperties().isEmpty());
        assertEquals(1, app.getProperties().get("number-property"));
        assertEquals("some value", app.getProperties().get("string-property"));
        assertEquals(3, app.getGroupIds().size());
        assertTrue(app.getApplicationIds().contains("four"));
        assertNull(app.getClientIds());
    }

    @Test(expected = KMigrationServerException.class)
    public void testParseError() throws Exception {
        ApplicationRepository repository = new ApplicationRepository(objectMapper, producer, consumer);
        repository.addToCache("sample-id", "this is not JSON");
    }
}