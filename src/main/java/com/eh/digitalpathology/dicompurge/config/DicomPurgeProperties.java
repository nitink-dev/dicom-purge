package com.eh.digitalpathology.dicompurge.config;

import com.eh.digitalpathology.dicompurge.service.WorkflowStatusEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "dicom")
public class DicomPurgeProperties {

    private Map<WorkflowStatusEnum, Duration> purgeDuration;

    public Map<WorkflowStatusEnum, Duration> getPurgeDuration() {
        return purgeDuration;
    }

    public void setPurgeDuration(Map<WorkflowStatusEnum, Duration> purgeDuration) {
        this.purgeDuration = purgeDuration;
    }
}
