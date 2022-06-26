package org.eventdrivendevelopment.kafka.migration.server.util;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.util.List;
import java.util.stream.Collectors;

public enum KafkaUtil {

    INSTANCE;

    public List<TopicPartition> getTopicPartitions(final String topicName, final Consumer<String, String> consumer) {
        List<PartitionInfo> partitionInfo = consumer.partitionsFor(topicName);
        return partitionInfo.stream().map(pi -> new TopicPartition(pi.topic(), pi.partition())).collect(Collectors.toList());
    }

}
