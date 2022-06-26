package org.eventdrivendevelopment.kafka.migration.server.controller;

import org.eventdrivendevelopment.kafka.migration.server.model.KMigration;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MigrationController {

    /**
     * Starts a migration session for the given application and optional environment
     * Starting a session performs a few tasks like ensuring no other migration for the same application is underway.
     * If a migration is underway, an error will be returned.
     * If no migrations are underway, a new session is created and the session-id is returned.
     * @param applicationId - the id of the application being migrated
     * @param environment - an optional environment in multi-purpose clusters
     * @return - a unique session id to be used in subsequent calls
     */
    @PostMapping(value = "/application/{applicationId}/migration/start")
    public String startSession(
            @PathVariable("id") final String applicationId,
            @RequestParam(required = false, name = "environment") final String environment) {
        return "";
    }

    /**
     * Terminates a migration session when all migrations have been executed
     * NOTE: The session is also terminated server-side when an error is encountered or after a given timeout.
     * @param applicationId - the id of the application being migrated (usually the repository name)
     * @param sessionId - the previously created session id
     * @param environment - an optional environment
     */
    @PostMapping(value = "/application/{applicationId}/migration/end")
    public void endSession(
            @PathVariable("applicationId") final String applicationId,
            @RequestParam("sessionId") final String sessionId,
            @RequestParam(required = false, name = "environment") final String environment) {

    }

    /**
     * Validate the migrations that will be executed
     * The validations that are performed are as follows:
     *  - Check that all migrations already executed are present in the list provided
     *  - Check that the migrations already executed have not been updated on the client's computer
     *  - If out-of-order migrations are not enabled, verify that all migrations are newer than the last executed
     *
     * @param applicationId - the application id
     * @param sessionId - the migration session
     * @param environment - an optional environment
     * @param migrations - a list of the migrations in JSON format
     * @return
     */
    @PostMapping(value = "/application/{applicationId}/migration/validate")
    public String validate(
            @PathVariable("applicationId") final String applicationId,
            @RequestParam("sessionId") final String sessionId,
            @RequestParam(required = false, name = "environment") final String environment,
            @RequestBody final List<Map<String, Object>> migrations) {
        return "";
    }

    /**
     * Execute the migrations
     * The validations that are performed are as follows:
     *  - Check that all migrations already executed are present in the list provided
     *  - Check that the migrations already executed have not been updated on the client's computer
     *  - If out-of-order migrations are not enabled, verify that all migrations are newer than the last executed
     *  NOTE: The migrations may contain "markers" that will be replaced with environment variables or other properties
     *
     * @param applicationId - the application id
     * @param sessionId - the migration session
     * @param environment - an optional environment
     * @param migrations - a list of the migrations in JSON format
     * @return
     */
    @PostMapping(value = "/application/{applicationId}/migration/migrate")
    public String migrate(
            @PathVariable("applicationId") final String applicationId,
            @RequestParam("sessionId") final String sessionId,
            @RequestParam(required = false, name = "environment") final String environment,
            @RequestBody final List<Map<String, Object>> migrations) {
        return "";
    }

    /**
     * Returns migration information for the given application and optional environment
     * @param applicationId - the application id
     * @param environment - an optional environment
     * @return - a list of executed migrations associated with the application
     */
    @GetMapping(value = "/application/{applicationId}/migration")
    public List<KMigration> info(
            @PathVariable("applicationId") final String applicationId,
            @RequestParam(required = false, name = "environment") final String environment) {
        return null;
    }
}
