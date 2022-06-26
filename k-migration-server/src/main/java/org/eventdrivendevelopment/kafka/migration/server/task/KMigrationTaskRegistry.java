package org.eventdrivendevelopment.kafka.migration.server.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.eventdrivendevelopment.kafka.migration.annotation.KMigrationTask;
import org.eventdrivendevelopment.kafka.migration.model.KMigrationData;
import org.eventdrivendevelopment.kafka.migration.server.exception.KMigrationServerException;
import org.eventdrivendevelopment.kafka.migration.server.model.KMigration;
import org.eventdrivendevelopment.kafka.migration.task.KMigrationTaskProcessor;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KMigrationTaskRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(KMigrationTaskRegistry.class);
    private static final String DEFAULT_SEARCH_PREFIX = "org.eventdrivendevelopment.kafka.migration";

    /**
     * The registry stores references to all supported migrations including custom ones referenced at runtime.
     */
    private final Map<String, KMigrationTaskRegistryItem> registry = new HashMap<>();

    /**
     * The migration context contains objects that will be required when running migrations like the admin client
     */
    private final Map<String, Object> migrationContext = new HashMap<>();

    /**
     * TODO: Is this supposed to be here or refactor somewhere else?
     * This object mapper will be configured to support the types associated with the migrations.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KMigrationTaskRegistry(final List<String> customTaskSearchPrefixes) {
        LOGGER.info("Starting Registry...");
        LOGGER.info("Adding standard migration Tasks...");
        addTasks(DEFAULT_SEARCH_PREFIX);
        if (customTaskSearchPrefixes != null && !customTaskSearchPrefixes.isEmpty()) {
            for (String prefix : customTaskSearchPrefixes) {
                LOGGER.info("Adding custom migration Tasks for prefix {}...", prefix);
                addTasks(prefix);
            }
        }
    }

    public Map<String, KMigrationTaskRegistryItem> getRegistry() {
        return registry;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    protected KMigration parseMigration(final String payload) {
        try {
            return objectMapper.readerFor(KMigration.class).readValue(payload);
        }
        catch (Exception e) {
            throw new KMigrationServerException("Unable to parse payload \n" + payload, e);
        }
    }

    protected void migrate(final String payload) {
        KMigration migration = parseMigration(payload);
        migrate(migration.getData());
    }

    protected <T extends KMigrationData> void migrate(final T migrationData) {
        final KMigrationTaskProcessor<T> processor = (KMigrationTaskProcessor<T>) this.registry.get(migrationData.getTaskType()).getTaskProcessor();
        processor.migrate(this.migrationContext, migrationData);
    }

    private void addTasks(final String prefix) {
        /*
         Although using the ConfigurationBuilder as indicated here (https://github.com/ronmamo/reflections/issues/373)
         properly works when running the spring-boot jar, the code finds annotated classes outside the provided package.
         This happens when running test cases and therefore we need to filter the tasks when processing them.
         */
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(prefix));
        Set<Class<?>> tasks = reflections.getTypesAnnotatedWith(KMigrationTask.class);
        tasks.stream().filter(task -> task.getPackageName().startsWith(prefix)).forEach(t -> {
            KMigrationTask annotation = t.getAnnotation(KMigrationTask.class);
            String taskType = annotation.taskType();
            LOGGER.info("Registering migration Task = '{}', taskDataClass = '{}'...", taskType, annotation.taskDataClass());
            if (registry.containsKey(taskType)) {
                throw new KMigrationServerException("Unable to register ok migration Task = " + taskType);
            }
            try {
                registry.put(taskType, new KMigrationTaskRegistryItem((KMigrationTaskProcessor<?>)t.getConstructor().newInstance(), annotation.taskDataClass()));
                objectMapper.registerSubtypes(new NamedType(annotation.taskDataClass(), taskType));
            }
            catch (Exception e) {
                throw new KMigrationServerException("Unable to create instance of migration Task " + t.toString(), e);
            }
        });
    }
}
