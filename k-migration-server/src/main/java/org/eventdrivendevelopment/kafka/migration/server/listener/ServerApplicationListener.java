package org.eventdrivendevelopment.kafka.migration.server.listener;

import org.eventdrivendevelopment.kafka.migration.server.repository.ApplicationRepository;
import org.eventdrivendevelopment.kafka.migration.server.service.InitializationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;

import java.util.Objects;

public class ServerApplicationListener implements ApplicationListener<ApplicationEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerApplicationListener.class);

    private static final Long START_TIMEOUT_MS = 30000L;
    private static final Long SLEEP_DELAY = 100L;

    private final InitializationService initializationService;
    private final ApplicationRepository applicationRepository;

    public ServerApplicationListener(
            final InitializationService initializationService,
            final ApplicationRepository applicationRepository) {
        this.initializationService = Objects.requireNonNull(initializationService);
        this.applicationRepository = applicationRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextStartedEvent) {
            LOGGER.info("Initializing server configuration");
            initializationService.initialize();

            LOGGER.info("Initializing Application repository");
            this.applicationRepository.initializeCache();
        }
    }
}
