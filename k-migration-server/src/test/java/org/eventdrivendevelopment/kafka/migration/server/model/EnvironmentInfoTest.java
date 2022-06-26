package org.eventdrivendevelopment.kafka.migration.server.model;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnvironmentInfoTest {

    @Test
    public void testEnvironmentInfo() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("email", "email@test.com");
        properties.put("number", 10);
        EnvironmentInfo environmentInfo = new EnvironmentInfo("environment", "PREFIX", "description", true, properties);
        assertEquals("environment", environmentInfo.getEnvironment());
        assertEquals("PREFIX", environmentInfo.getPrefix());
        assertEquals("description", environmentInfo.getDescription());
        assertTrue(environmentInfo.isApprovalsRequired());
        assertTrue(environmentInfo.getProperties().containsKey("number"));
        assertEquals(10, environmentInfo.getProperties().get("number"));
        assertTrue(environmentInfo.toString().contains("environment='environment'"));
    }

}