package org.eventdrivendevelopment.kafka.migration.server.service;

import org.eventdrivendevelopment.kafka.migration.server.model.ApplicationInfo;
import org.eventdrivendevelopment.kafka.migration.server.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<ApplicationInfo> all() {
        return this.applicationRepository.all();
    }

    public void add(final ApplicationInfo applicationInfo) {
        this.applicationRepository.add(applicationInfo);
    }

}
