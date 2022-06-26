package org.eventdrivendevelopment.kafka.migration.server.controller;

import org.eventdrivendevelopment.kafka.migration.server.model.LogInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LogController {

    /**
     * Get application log
     * TODO: implement paging logic
     * @param applicationId - the unique id for the application
     * @return a list of log entries
     */
    @GetMapping(value = "/log/{applicationId}")
    public List<LogInfo> getLog(
            @PathVariable("applicationId") final String applicationId,
            @RequestParam(name = "environment", required = false) final String environment,
            @RequestParam(name = "offset", required = false) final long offset,
            @RequestParam(name = "count", required = false) final int count) {
        return null;
    }

}
