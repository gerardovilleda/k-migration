package org.eventdrivendevelopment.kafka.migration.server.service;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.TopicConfig;
import org.eventdrivendevelopment.kafka.migration.server.exception.KMigrationServerException;
import org.eventdrivendevelopment.kafka.migration.server.util.NameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Verifies that the server is properly configured for other things to start.
 */
public class InitializationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitializationService.class);

    private static final int SINGLE_PARTITION_TOPIC = 1;

    private final AdminClient adminClient;

    public InitializationService(final AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    public void initialize() {
        LOGGER.info("Initializing KMigration server...");
        Set<String> topicNames = getTopicNames();

        String applicationsTopicName = NameUtil.INSTANCE.getApplicationsTopicName();
        if (!topicNames.contains(applicationsTopicName)) {
            LOGGER.info("Creating applications topic {}...", applicationsTopicName);
            createSystemTopic(applicationsTopicName);
        }
        else {
            validateTopicConfig(applicationsTopicName);
        }

        String environmentsTopicName = NameUtil.INSTANCE.getEnvironmentsTopicName();
        if (!topicNames.contains(environmentsTopicName)) {
            createSystemTopic(environmentsTopicName);
        }
        else {
            validateTopicConfig(environmentsTopicName);
        }
    }

    public void validateEnvironments(final List<String> environmentPrefixes) {

    }

    protected void createSystemTopic(final String topicName) {
        LOGGER.info("Creating system topic - {}", topicName);
        final NewTopic newTopic = new NewTopic(topicName, Optional.of(SINGLE_PARTITION_TOPIC), Optional.empty());
        newTopic.configs(Collections.singletonMap(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT));
        try {
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        }
        catch (final InterruptedException | ExecutionException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new KMigrationServerException("Unable to create topic - InterruptedException - " + topicName, e);
        }
    }

    protected void validateTopicConfig(final String topicName) {
        try {
            final Map<String, TopicDescription> topicDescriptionMap = adminClient.describeTopics(Collections.singletonList(topicName)).allTopicNames().get();
            if (topicDescriptionMap.get(topicName).partitions().size() != SINGLE_PARTITION_TOPIC) {
                throw new KMigrationServerException("Invalid partition count for topic - " + topicName);
            }
            ConfigResource topicConfig = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
            final Map<ConfigResource, Config> topicConfiguration = adminClient.describeConfigs(Collections.singletonList(topicConfig)).all().get();
            String cleanupPolicy = topicConfiguration.get(topicConfig).get(TopicConfig.CLEANUP_POLICY_CONFIG).value();
            if (null == cleanupPolicy || !cleanupPolicy.equals(TopicConfig.CLEANUP_POLICY_COMPACT)) {
                throw new KMigrationServerException("Missing compact topic cleanup policy configuration - " + topicName);
            }
        }
        catch (final InterruptedException | ExecutionException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new KMigrationServerException("Unable to retrieve topic description - InterruptedException - " + topicName, e);
        }
    }

    protected Set<String> getTopicNames() {
        try {
            return this.adminClient.listTopics().names().get();
        }
        catch (final InterruptedException | ExecutionException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new KMigrationServerException("Unable to retrieve topics - InterruptedException", e);
        }
    }
}
