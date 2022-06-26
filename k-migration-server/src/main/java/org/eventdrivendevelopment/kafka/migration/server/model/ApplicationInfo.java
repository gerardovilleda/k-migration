package org.eventdrivendevelopment.kafka.migration.server.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Provides information about an application registered in the cluster.
 * It helps organize the data collected but is also required when running migrations on the cluster.
 */
@Validated
public class ApplicationInfo {

    /**
     * The repository name is the unique ID for the application on the cluster
     * A single application can run multiple migrations on the cluster.
     * It can also run the same migrations under a different "environment" in the cluster
     * in cases where the cluster is used for multiple phases of development (e.g. DEV, TEST).
     */
    @NotNull
    @NotEmpty
    private final String id;

    /**
     * A brief description of the purpose for the application
     */
    @NotNull
    @NotEmpty
    private final String description;

    /**
     * A responsible owner for the application (e-mail distribution list)
     */
    @NotNull
    @NotEmpty
    private final String owner;

    /**
     * Miscellaneous properties free to use. For instance, product, code, domain, etc.
     */
    private final Map<String, Object> properties;

    /**
     * If available, the list of consumer groups that this application uses when consuming events.
     */
    private final List groupIds;

    /**
     * If available, the list of application Ids referenced by this application (Kafka streams)
     */
    private final List applicationIds;

    /**
     * If available, the list of client Ids referenced by this application when producing events
     */
    private final List clientIds;

    /**
     *
     * @param id - The name of the repository on the source control system
     * @param description - The description of the application
     * @param owner - An e-mail distribution list of the owning team/individual
     * @param properties - Miscellaneous properties that describe the application
     * @param groupIds - If available, the consumer groups that the application references
     * @param applicationIds - If available, the application Ids referenced by this application
     * @param clientIds - If available, the list of client Ids referenced by this application
     */
    @JsonCreator
    public ApplicationInfo(
            @JsonProperty("id") final String id,
            @JsonProperty("description") final String description,
            @JsonProperty("owner") final String owner,
            @JsonProperty("properties") final Map<String, Object> properties,
            @JsonProperty("groupIds") final List groupIds,
            @JsonProperty("applicationIds") final List applicationIds,
            @JsonProperty("clientIds") final List clientIds) {
        this.id = id;
        this.description = description;
        this.owner = owner;
        this.properties = properties;
        this.groupIds = groupIds;
        this.applicationIds = applicationIds;
        this.clientIds = clientIds;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getOwner() {
        return owner;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public List getGroupIds() {
        return groupIds;
    }

    public List getApplicationIds() {
        return applicationIds;
    }

    public List getClientIds() {
        return clientIds;
    }
}
