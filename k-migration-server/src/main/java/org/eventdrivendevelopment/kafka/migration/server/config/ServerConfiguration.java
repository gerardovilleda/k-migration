package org.eventdrivendevelopment.kafka.migration.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.eventdrivendevelopment.kafka.migration.server.listener.ServerApplicationListener;
import org.eventdrivendevelopment.kafka.migration.server.repository.ApplicationRepository;
import org.eventdrivendevelopment.kafka.migration.server.service.InitializationService;
import org.eventdrivendevelopment.kafka.migration.server.task.KMigrationTaskRegistry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(ServerConfigurationProperties.class)
public class ServerConfiguration {

    @Bean
    public KMigrationTaskRegistry migrationTaskRegistry(final ServerConfigurationProperties serverConfigurationProperties) {
        return new KMigrationTaskRegistry(serverConfigurationProperties.getCustomTaskSearchPrefixes());
    }

    @Bean
    public ObjectMapper objectMapper(KMigrationTaskRegistry migrationTaskRegistry) {
        return migrationTaskRegistry.getObjectMapper();
    }

    @Bean
    public AdminClient adminClient(final ServerConfigurationProperties serverConfigurationProperties) {
        Map<String, Object> adminConfig = new HashMap<>(serverConfigurationProperties.getAdmin());
        adminConfig.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, serverConfigurationProperties.getBootstrapServers());
        return AdminClient.create(adminConfig);
    }

    @Bean
    public ApplicationRepository applicationRepository(
            final ServerConfigurationProperties serverConfigurationProperties,
            final ObjectMapper objectMapper) {
        return new ApplicationRepository(objectMapper, getKafkaProducer(serverConfigurationProperties), getKafkaConsumer(serverConfigurationProperties));
    }

    @Bean
    public InitializationService initializationService(final AdminClient adminClient) {
        return new InitializationService(adminClient);
    }

    @Bean
    public ServerApplicationListener serverApplicationListener(
            final InitializationService initializationService,
            final ApplicationRepository applicationRepository) {
        return new ServerApplicationListener(initializationService, applicationRepository);
    }

    private Producer<String, String> getKafkaProducer(final ServerConfigurationProperties serverConfigurationProperties) {
        Map<String, Object> producerConfig = new HashMap<>(serverConfigurationProperties.getProducer());
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverConfigurationProperties.getBootstrapServers());
        return new KafkaProducer<String, String>(producerConfig, new StringSerializer(), new StringSerializer());
    }

    private Consumer<String, String> getKafkaConsumer(final ServerConfigurationProperties serverConfigurationProperties) {
        Map<String, Object> consumerConfig = new HashMap<>(serverConfigurationProperties.getConsumer());
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverConfigurationProperties.getBootstrapServers());
        return new KafkaConsumer<String, String>(consumerConfig, new StringDeserializer(), new StringDeserializer());
    }

}
