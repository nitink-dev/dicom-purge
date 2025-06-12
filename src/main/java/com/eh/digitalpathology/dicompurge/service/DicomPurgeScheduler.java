package com.eh.digitalpathology.dicompurge.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

@Component
public class DicomPurgeScheduler implements SchedulingConfigurer {

    @Value("${scheduler.purge-cron}")
    private String purgeCron;

    private final DicomPurgeService dicomPurgeService;

    public DicomPurgeScheduler(DicomPurgeService dicomPurgeService) {
        this.dicomPurgeService = dicomPurgeService;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addCronTask(() -> dicomPurgeService.purgeExpiredDicomFiles(), purgeCron);
    }
}
