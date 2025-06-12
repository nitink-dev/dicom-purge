package com.eh.digitalpathology.dicompurge.service;

import com.eh.digitalpathology.dicompurge.entity.DicomInstance;
import com.eh.digitalpathology.dicompurge.entity.DicomInstanceRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DicomInstanceCheckService {

    private static final Logger log = LoggerFactory.getLogger(DicomInstanceCheckService.class);
    private final DicomInstanceRepository dicomInstanceRepository;

    public DicomInstanceCheckService(DicomInstanceRepository dicomInstanceRepository) {
        this.dicomInstanceRepository = dicomInstanceRepository;
    }

    /**
     * Scheduled to run every 5 minutes.
     * Cron format: second, minute, hour, day of month, month, day(s) of week
     */
    // @Scheduled(cron = "0 */5 * * * *") // Every 5 minutes for now testing...
    @PostConstruct
    public void checkForUnenrichedDicomInstances() {
        log.info("Checking for DICOM instances with null barcode...");

        List<DicomInstance> unenrichedInstances = dicomInstanceRepository.findByBarcodeIsNull();

        if (unenrichedInstances.isEmpty()) {
            log.info("No unenriched DICOM instances found.");
        } else {
            log.info("Found {} unenriched DICOM instances.", unenrichedInstances.size());
            unenrichedInstances.forEach(instance -> {
                log.debug("Unenriched Instance ID: {}", instance.getId());
                // Add any additional logic here — e.g., mark, log, send alerts, or move to deletion queue
            });
        }
    }
}
