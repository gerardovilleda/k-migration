package org.eventdrivendevelopment.kafka.migration.server.model;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LogInfoTest {

    @Test
    public void testLogInfo() {
        Map<String, String> headers = new HashMap<>();
        headers.put("headerKey", "headerValue");
        LogInfo logInfo = new LogInfo(1, 2, 3, "applicationId", headers, "text");
        assertEquals(1, logInfo.getPartition());
        assertEquals(2, logInfo.getOffset());
        assertEquals(3, logInfo.getTimestamp());
        assertEquals("applicationId", logInfo.getApplicationId());
        assertEquals("text", logInfo.getText());
        assertTrue(logInfo.getHeaders().containsKey("headerKey"));
        assertEquals("headerValue", logInfo.getHeaders().get("headerKey"));
        assertTrue(logInfo.toString().contains("timestamp=3"));
    }
}