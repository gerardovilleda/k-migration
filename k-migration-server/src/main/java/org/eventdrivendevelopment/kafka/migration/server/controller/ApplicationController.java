package org.eventdrivendevelopment.kafka.migration.server.controller;

import org.eventdrivendevelopment.kafka.migration.server.model.ApplicationInfo;
import org.eventdrivendevelopment.kafka.migration.server.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApplicationController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /**
     * Retrieve application information for all registered applications
     * @return - the information for all applications
     */
    @GetMapping(value = "/application")
    public List<ApplicationInfo> getApplications() {
        return this.applicationService.all();
    }

    /**
     * Retrieve application information for a single application
     * @param applicationId - the unique ID for the application (usually the repository name)
     * @return - the application information
     */
    @GetMapping(value = "/application/{applicationId}")
    public ApplicationInfo getApplication(@PathVariable("applicationId") final String applicationId) {
        return null;
    }

    /**
     * Register a new application in the cluster
     * An application must be registered before any migration or other similar activity can be performed.
     * @param applicationInfo - the details of the new application
     * @return - a unique Key that identifies the application to be used in subsequent requests
     */
    @PostMapping(value = "/application")
    public void createApplication(@RequestBody final ApplicationInfo applicationInfo) {
        this.applicationService.add(applicationInfo);
    }

    /**
     * Updates the information associated with an application
     * NOTE: It may be useful to have separate services to update the properties of the application
     * @param applicationId - the unique id of the application to update
     * @param applicationInfo - the updated information
     * @return - it will echo the information received
     */
    @PutMapping(value = "/application/{applicationId}")
    public ApplicationInfo updateApplication(
            @PathVariable("applicationId") final String applicationId,
            @RequestBody final ApplicationInfo applicationInfo) {
        return null;
    }

    /**
     * Deletes the provided application
     * @param applicationId - the unique id of the application to delete
     */
    @DeleteMapping(value = "/application/{applicationId}")
    public void deleteApplication(@PathVariable("applicationId") final String applicationId) {

    }
}
