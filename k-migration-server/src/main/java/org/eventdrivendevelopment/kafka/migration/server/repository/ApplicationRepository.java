package org.eventdrivendevelopment.kafka.migration.server.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.eventdrivendevelopment.kafka.migration.server.exception.KMigrationServerException;
import org.eventdrivendevelopment.kafka.migration.server.model.ApplicationInfo;
import org.eventdrivendevelopment.kafka.migration.server.repository.consumer.TopicConsumer;
import org.eventdrivendevelopment.kafka.migration.server.util.KafkaUtil;
import org.eventdrivendevelopment.kafka.migration.server.util.NameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class ApplicationRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationRepository.class);

    private final Map<String, ApplicationInfo> applications = new TreeMap<>();
    private final ObjectMapper objectMapper;
    private final Producer<String, String> producer;
    private final Consumer<String, String> consumer;

    public ApplicationRepository(
            final ObjectMapper objectMapper,
            final Producer<String, String> producer,
            final Consumer<String, String> consumer) {
        this.objectMapper = objectMapper;
        this.producer = producer;
        this.consumer = consumer;
    }

    public void add(final ApplicationInfo applicationInfo) {
        Objects.requireNonNull(applicationInfo);
        if (applications.containsKey(applicationInfo.getId())) {
            throw new KMigrationServerException("Duplicate application id found  " + applicationInfo.getId());
        }
        try {
            producer.send(new ProducerRecord<>(NameUtil.INSTANCE.getApplicationsTopicName(), applicationInfo.getId(), toJson(applicationInfo))).get();
            producer.flush();
            applications.put(applicationInfo.getId(), applicationInfo);
        }
        catch (final InterruptedException | ExecutionException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new KMigrationServerException("Unable to publish application", e);
        }
    }

    /**
     * This method is called at startup to read all the events from the topic and initialize the map of Applications.
     * This could be invoked again to reload the Applications
     */
    public void initializeCache() {
        this.applications.clear();
        try (final TopicConsumer<String, String> topicConsumer = new TopicConsumer<>(this.consumer, NameUtil.INSTANCE.getApplicationsTopicName())) {
            topicConsumer.start();
            while (topicConsumer.hasNext()) {
                final List<ConsumerRecord<String, String>> records = topicConsumer.next();
                records.stream().forEach(r -> {
                    if (r.value() == null) {
                        this.applications.remove(r.key());
                    }
                    else {
                        this.applications.put(r.key(), parse(r.value()));
                    }
                });
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addToCache(final String key, final String value) {
        LOGGER.debug("Adding to cache key={}, value={}", key, value);
        applications.put(key, parse(value));
    }

    public List<ApplicationInfo> all() {
        List<ApplicationInfo> ret = new ArrayList<>();
        applications.forEach((k, v) -> ret.add(v));
        return ret;
    }

    protected ApplicationInfo parse(final String value) {
        try {
            return objectMapper.readValue(value, ApplicationInfo.class);
        }
        catch (JsonProcessingException e) {
            throw new KMigrationServerException("Unable to parse AccountInfo", e);
        }
    }

    protected String toJson(final ApplicationInfo applicationInfo) {
        try {
            return this.objectMapper.writeValueAsString(applicationInfo);
        }
        catch (JsonProcessingException e) {
            throw new KMigrationServerException("Unable to format as Json", e);
        }
    }
}
