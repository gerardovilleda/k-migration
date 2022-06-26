package org.eventdrivendevelopment.kafka.migration.server.exception;

public class KMigrationServerException extends RuntimeException {

    public KMigrationServerException(final String message) {
        super(message);
    }

    public KMigrationServerException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
