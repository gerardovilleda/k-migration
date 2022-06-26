# Notes
Here are some random notes I've taken while writing this tool. 

## Streams
Using streams to read Application and Environment data sounded like a good idea. 
However, it is important to "rewind" and read all events at startup. This is not easily done with KafkaStreams.
I opted to read the events using the Consumer API instead. 

### KafkaStreams Configuration
This was the proposed configuration for the Stream

    @Bean
    public KafkaStreams kafkaStreams(
            final ServerConfigurationProperties serverConfigurationProperties,
            final ApplicationRepository applicationRepository) {
        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        final Consumed<String, String> consumedConfiguration = Consumed.with(Serdes.String(), Serdes.String());

        /**
         * Accessing header values will require to use a ValueTransformerWithKey. Would that be required?
         * https://stackoverflow.com/questions/61270063/kafka-streams-how-to-get-the-kafka-headers
         */
        final KStream<String, String> applications = streamsBuilder.stream(NameUtil.INSTANCE.getApplicationsTopicName(), consumedConfiguration);
        applications.foreach(applicationRepository::addToCache);

        final Properties properties = new Properties();
        properties.putAll(serverConfigurationProperties.getStream());
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, serverConfigurationProperties.getBootstrapServers());
        return new KafkaStreams(streamsBuilder.build(), properties);
    }

### ApplicationListener
As indicated in the comment, the `cleanUp` method will not reset the offsets. 
There is a tool to execute this task but I prefer to avoid having to depend on that (or write all that code)
https://www.confluent.io/blog/data-reprocessing-with-kafka-streams-resetting-a-streams-application/

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextStartedEvent) {
            LOGGER.info("Cleaning previous KafkaStreams local state (will not reset offsets)...");
            kafkaStreams.cleanUp();
            LOGGER.info("Starting KafkaStreams...");
            kafkaStreams.start();
            Instant start = Instant.now();
            while (!kafkaStreams.state().isRunningOrRebalancing()) {
                if (Duration.between(start, Instant.now()).toMillis() > START_TIMEOUT_MS) {
                    throw new KMigrationServerException("Unable to start KafkaStreams.");
                }
                try {
                    Thread.sleep(SLEEP_DELAY);
                }
                catch (Exception e) {
                    throw new CompletionException("Likely an interrupted exception", e);
                }
            }
        }
    }
