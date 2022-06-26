package org.eventdrivendevelopment.kafka.migration.server.controller;

import org.eventdrivendevelopment.kafka.migration.server.model.EnvironmentInfo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EnvironmentController {

    @GetMapping(value = "/environment")
    public List<EnvironmentInfo> getEnvironments() {
        return new ArrayList<>();
    }

    @GetMapping(value = "/environment/{prefix}")
    public EnvironmentInfo getEnvironment(@PathVariable("prefix") final String prefix) {
        return null;
    }

    @PostMapping(value = "/environment")
    public EnvironmentInfo createEnvironment(@RequestBody final EnvironmentInfo environmentInfo) {
        return null;
    }

    @PutMapping(value = "/environment/{prefix}")
    public EnvironmentInfo updateEnvironment(@PathVariable("prefix") final String prefix, @RequestBody final EnvironmentInfo environmentInfo) {
        return null;
    }

    @DeleteMapping(value = "/environment/{prefix}")
    public void deleteEnvironment(@PathVariable("prefix") final String prefix) {

    }
}
