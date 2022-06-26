package org.eventdrivendevelopment.kafka.migration.server.service;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.TopicConfig;
import org.eventdrivendevelopment.kafka.migration.server.exception.KMigrationServerException;
import org.eventdrivendevelopment.kafka.migration.server.util.NameUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InitializationServiceTest {

    @Mock
    private AdminClient adminClient;

    @Mock
    private ListTopicsResult listTopicsResult;

    @Mock
    private KafkaFuture<Set<String>> setKafkaFuture;

    @Mock
    private DescribeTopicsResult describeTopicsResult;

    @Mock
    private KafkaFuture<Map<String, TopicDescription>> topicDescriptionKafkaFuture;

    @Mock
    private Map<String, TopicDescription> topicDescriptionMap;

    @Mock
    private DescribeConfigsResult describeConfigsResult;

    @Mock
    private KafkaFuture<Map<ConfigResource, Config>> configResourceKafkaFuture;

    @Mock
    private Map<ConfigResource, Config> configResourceConfigMap;

    @Mock
    private CreateTopicsResult createTopicsResult;

    @Mock
    private KafkaFuture<Void> voidKafkaFuture;

    @Test
    public void testSuccessfulInitialization() throws Exception {
        InitializationService service = new InitializationService(adminClient);

        // The list of topics
        Set<String> names = new HashSet<>();
        names.add(NameUtil.INSTANCE.getApplicationsTopicName());
        names.add(NameUtil.INSTANCE.getEnvironmentsTopicName());
        when(setKafkaFuture.get()).thenReturn(names);
        when(listTopicsResult.names()).thenReturn(setKafkaFuture);
        when(adminClient.listTopics()).thenReturn(listTopicsResult);

        // Topic partitions
        TopicPartitionInfo topicPartitionInfo = new TopicPartitionInfo(0, null, new ArrayList<>(), new ArrayList<>());
        TopicDescription topicDescription = new TopicDescription("topic-name", false, Collections.singletonList(topicPartitionInfo));
        when(topicDescriptionMap.get(anyString())).thenReturn(topicDescription);
        when(topicDescriptionKafkaFuture.get()).thenReturn(topicDescriptionMap);
        when(describeTopicsResult.allTopicNames()).thenReturn(topicDescriptionKafkaFuture);
        when(adminClient.describeTopics(anyCollection())).thenReturn(describeTopicsResult);

        // Topic configuration
        Config config = new Config(Collections.singletonList(new ConfigEntry(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT)));
        when(configResourceConfigMap.get(any(ConfigResource.class))).thenReturn(config);
        when(configResourceKafkaFuture.get()).thenReturn(configResourceConfigMap);
        when(describeConfigsResult.all()).thenReturn(configResourceKafkaFuture);
        when(adminClient.describeConfigs(anyCollection())).thenReturn(describeConfigsResult);
        service.initialize();
    }

    @Test(expected = KMigrationServerException.class)
    public void testErrorTooManyPartitions() throws Exception {
        InitializationService service = new InitializationService(adminClient);

        // The list of topics
        Set<String> names = new HashSet<>();
        names.add(NameUtil.INSTANCE.getApplicationsTopicName());
        names.add(NameUtil.INSTANCE.getEnvironmentsTopicName());
        when(setKafkaFuture.get()).thenReturn(names);
        when(listTopicsResult.names()).thenReturn(setKafkaFuture);
        when(adminClient.listTopics()).thenReturn(listTopicsResult);

        // Topic partitions
        TopicPartitionInfo topicPartitionInfo = new TopicPartitionInfo(0, null, new ArrayList<>(), new ArrayList<>());
        List<TopicPartitionInfo> topicPartitionInfoList = new ArrayList<>();
        topicPartitionInfoList.add(topicPartitionInfo);
        topicPartitionInfoList.add(topicPartitionInfo);
        TopicDescription topicDescription = new TopicDescription("topic-name", false, topicPartitionInfoList);
        when(topicDescriptionMap.get(anyString())).thenReturn(topicDescription);
        when(topicDescriptionKafkaFuture.get()).thenReturn(topicDescriptionMap);
        when(describeTopicsResult.allTopicNames()).thenReturn(topicDescriptionKafkaFuture);
        when(adminClient.describeTopics(anyCollection())).thenReturn(describeTopicsResult);

        service.initialize();
    }

    @Test(expected = KMigrationServerException.class)
    public void testNotACompactedTopic() throws Exception {
        InitializationService service = new InitializationService(adminClient);

        // The list of topics
        Set<String> names = new HashSet<>();
        names.add(NameUtil.INSTANCE.getApplicationsTopicName());
        names.add(NameUtil.INSTANCE.getEnvironmentsTopicName());
        when(setKafkaFuture.get()).thenReturn(names);
        when(listTopicsResult.names()).thenReturn(setKafkaFuture);
        when(adminClient.listTopics()).thenReturn(listTopicsResult);

        // Topic partitions
        TopicPartitionInfo topicPartitionInfo = new TopicPartitionInfo(0, null, new ArrayList<>(), new ArrayList<>());
        TopicDescription topicDescription = new TopicDescription("topic-name", false, Collections.singletonList(topicPartitionInfo));
        when(topicDescriptionMap.get(anyString())).thenReturn(topicDescription);
        when(topicDescriptionKafkaFuture.get()).thenReturn(topicDescriptionMap);
        when(describeTopicsResult.allTopicNames()).thenReturn(topicDescriptionKafkaFuture);
        when(adminClient.describeTopics(anyCollection())).thenReturn(describeTopicsResult);

        // Topic configuration
        Config config = new Config(Collections.singletonList(new ConfigEntry(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE)));
        when(configResourceConfigMap.get(any(ConfigResource.class))).thenReturn(config);
        when(configResourceKafkaFuture.get()).thenReturn(configResourceConfigMap);
        when(describeConfigsResult.all()).thenReturn(configResourceKafkaFuture);
        when(adminClient.describeConfigs(anyCollection())).thenReturn(describeConfigsResult);
        service.initialize();
    }

    @Test
    public void testSuccessfulInitializationCreatingTopics() throws Exception {
        InitializationService service = new InitializationService(adminClient);

        // The list of topics
        Set<String> names = new HashSet<>();
        when(setKafkaFuture.get()).thenReturn(names);
        when(listTopicsResult.names()).thenReturn(setKafkaFuture);
        when(adminClient.listTopics()).thenReturn(listTopicsResult);

        // Create the topics
        when(createTopicsResult.all()).thenReturn(voidKafkaFuture);
        when(adminClient.createTopics(anyCollection())).thenReturn(createTopicsResult);
        service.initialize();
    }

    @Test(expected = KMigrationServerException.class)
    public void testFailedToCreateTopics() throws Exception {
        InitializationService service = new InitializationService(adminClient);

        // The list of topics
        Set<String> names = new HashSet<>();
        when(setKafkaFuture.get()).thenReturn(names);
        when(listTopicsResult.names()).thenReturn(setKafkaFuture);
        when(adminClient.listTopics()).thenReturn(listTopicsResult);

        // Create the topics
        when(voidKafkaFuture.get()).thenThrow(new InterruptedException("Interrupted exception"));
        when(createTopicsResult.all()).thenReturn(voidKafkaFuture);
        when(adminClient.createTopics(anyCollection())).thenReturn(createTopicsResult);
        service.initialize();
    }

    @Test(expected = KMigrationServerException.class)
    public void testErrorRetrievingTopics() throws Exception {
        InitializationService service = new InitializationService(adminClient);

        // The list of topics
        when(setKafkaFuture.get()).thenThrow(new InterruptedException("Unable to get topics"));
        when(listTopicsResult.names()).thenReturn(setKafkaFuture);
        when(adminClient.listTopics()).thenReturn(listTopicsResult);

        service.initialize();
    }

    @Test(expected = KMigrationServerException.class)
    public void testErrorDescribingTopics() throws Exception {
        InitializationService service = new InitializationService(adminClient);

        // The list of topics
        Set<String> names = new HashSet<>();
        names.add(NameUtil.INSTANCE.getApplicationsTopicName());
        names.add(NameUtil.INSTANCE.getEnvironmentsTopicName());
        when(setKafkaFuture.get()).thenReturn(names);
        when(listTopicsResult.names()).thenReturn(setKafkaFuture);
        when(adminClient.listTopics()).thenReturn(listTopicsResult);

        // Topic partitions
        when(topicDescriptionKafkaFuture.get()).thenThrow(new InterruptedException("Unable to describe topics"));
        when(describeTopicsResult.allTopicNames()).thenReturn(topicDescriptionKafkaFuture);
        when(adminClient.describeTopics(anyCollection())).thenReturn(describeTopicsResult);

        service.initialize();
    }

}