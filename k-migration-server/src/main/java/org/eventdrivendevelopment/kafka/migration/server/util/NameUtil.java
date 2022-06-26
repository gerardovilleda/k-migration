package org.eventdrivendevelopment.kafka.migration.server.util;

import java.util.Objects;

public enum NameUtil {

    INSTANCE;

    private static final String TOPIC_PREFIX = "_kmigration";
    private static final String NAME_SEPARATOR = "_";

    /**
     * Application and environment are global configuration topics
     */
    private static final String APPLICATIONS = "applications";
    private static final String ENVIRONMENTS = "environments";

    /**
     * History, execution, pending-review and approved are environment specific topics
     */
    private static final String MIGRATION_HISTORY = "migration_history";
    private static final String MIGRATIONS_PENDING_REVIEW = "migrations_pending_review";
    private static final String APPROVED_MIGRATIONS = "approved_migrations";

    public String getApplicationsTopicName() {
        return TOPIC_PREFIX + NAME_SEPARATOR + APPLICATIONS;
    }

    public String getEnvironmentsTopicName() {
        return TOPIC_PREFIX + NAME_SEPARATOR + ENVIRONMENTS;
    }

    public String getMigrationHistoryTopicName(final String prefix) {
        return cleanPrefix(prefix) + NAME_SEPARATOR + MIGRATION_HISTORY;
    }

    public String getMigrationsPendingReviewTopicName(final String prefix) {
        return cleanPrefix(prefix) + NAME_SEPARATOR + MIGRATIONS_PENDING_REVIEW;
    }

    public String getApprovedMigrationsTopicName(final String prefix) {
        return cleanPrefix(prefix) + NAME_SEPARATOR + APPROVED_MIGRATIONS;
    }

    private String cleanPrefix(final String prefix) {
        String tmp = Objects.requireNonNull(prefix).trim();
        if (tmp.isEmpty()) {
            throw new IllegalArgumentException("Prefix cannot be empty");
        }
        return tmp;
    }
}
