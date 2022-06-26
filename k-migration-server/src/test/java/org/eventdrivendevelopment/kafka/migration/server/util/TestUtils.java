package org.eventdrivendevelopment.kafka.migration.server.util;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {

    public static String testResourceFileText(final String fileName) throws Exception {
        URL url = TestUtils.class.getClassLoader().getResource(fileName);
        return Files.readString(Paths.get(url.toURI()));
    }

}
