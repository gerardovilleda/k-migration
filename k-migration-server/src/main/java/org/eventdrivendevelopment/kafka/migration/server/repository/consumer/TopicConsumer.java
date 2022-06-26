package org.eventdrivendevelopment.kafka.migration.server.repository.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class TopicConsumer<K, V> implements Iterator<List<ConsumerRecord<K, V>>>, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicConsumer.class);

    protected final Consumer<K, V> consumer;
    protected final String topicName;
    protected final Duration pollTimeout;

    protected Map<TopicPartition, Long> targetOffsets = new HashMap<>();

    public TopicConsumer(Consumer<K, V> consumer, String topicName) {
        this(consumer, topicName, Duration.ofMillis(Long.MAX_VALUE));
    }

    public TopicConsumer(final Consumer<K, V> consumer, final String topicName, final Duration pollTimeout) {
        this.consumer = Objects.requireNonNull(consumer);
        this.topicName = Objects.requireNonNull(topicName);
        this.pollTimeout = Objects.requireNonNull(pollTimeout);
    }

    public void start() {
        List<TopicPartition> topicPartitions = getTopicPartitions();
        consumer.assign(topicPartitions);
        consumer.seekToBeginning(topicPartitions);
        // Find the last offset for each of the partitions
        Map<TopicPartition, Long> beginningOffsets = consumer.beginningOffsets(consumer.assignment());
        Map<TopicPartition, Long> endOffsets = consumer.endOffsets(consumer.assignment());
        for (Map.Entry<TopicPartition, Long> partition : beginningOffsets.entrySet()) {
            final Long beginOffset = partition.getValue();
            final Long endOffset = endOffsets.get((partition.getKey()));
            if (endOffset != 0 && !beginOffset.equals(endOffset)) {
                LOGGER.debug("Adding target offset = {} for partition {}", endOffset, partition.getKey().partition());
                targetOffsets.put(partition.getKey(), endOffset);
            }
        }
    }

    @Override
    public void close() throws Exception {
        consumer.close();
    }

    @Override
    public boolean hasNext() {
        return !targetOffsets.isEmpty();
    }

    @Override
    public List<ConsumerRecord<K, V>> next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Done polling records");
        }
        final List<ConsumerRecord<K, V>> ret = new ArrayList<>();
        final ConsumerRecords<K, V> consumerRecords = consumer.poll(this.pollTimeout);
        final Set<TopicPartition> completedPartitions = new HashSet<>();
        for (Map.Entry<TopicPartition, Long> partition : targetOffsets.entrySet()) {
            final List<ConsumerRecord<K, V>> partitionRecords = consumerRecords.records(partition.getKey());
            for (final ConsumerRecord<K, V> partitionRecord : partitionRecords) {
                if (partitionRecord.offset() < partition.getValue()) {
                    ret.add(partitionRecord);
                }
                if ((partitionRecord.offset() + 1) >= partition.getValue()) {
                    LOGGER.debug("Done with this partition = {}, offset = {}", partition.getKey().partition(), partitionRecord.offset());
                    completedPartitions.add(partition.getKey());
                }
            }
        }
        if (!completedPartitions.isEmpty()) {
            completedPartitions.forEach(p -> targetOffsets.remove(p));
            consumer.assign(targetOffsets.keySet());
        }
        return ret;
    }

    protected List<TopicPartition> getTopicPartitions() {
        List<PartitionInfo> partitionInfo = consumer.partitionsFor(topicName);
        return partitionInfo.stream().map(pi -> new TopicPartition(pi.topic(), pi.partition())).collect(Collectors.toList());
    }

}
