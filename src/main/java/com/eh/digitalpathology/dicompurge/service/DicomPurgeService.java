package com.eh.digitalpathology.dicompurge.service;

import com.eh.digitalpathology.dicompurge.config.DicomPurgeProperties;
import com.eh.digitalpathology.dicompurge.entity.DicomInstance;
import com.eh.digitalpathology.dicompurge.entity.DicomInstanceRepository;
import com.eh.digitalpathology.dicompurge.entity.EnrichmentPurge;
import com.eh.digitalpathology.dicompurge.entity.EnrichmentPurgeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DicomPurgeService {

    @Value("${optfile.server.path}")
    private String fileRootPath;

    private final DicomInstanceRepository dicomInstanceRepository;
    private final EnrichmentPurgeRepository enrichmentPurgeRepository;
    private final Map<WorkflowStatusEnum, Duration> purgeDurations;

    @Autowired
    public DicomPurgeService(DicomInstanceRepository dicomInstanceRepository,
                             EnrichmentPurgeRepository enrichmentPurgeRepository,
                             DicomPurgeProperties dicomPurgeProperties) {
        this.dicomInstanceRepository = dicomInstanceRepository;
        this.enrichmentPurgeRepository = enrichmentPurgeRepository;
        this.purgeDurations = dicomPurgeProperties.getPurgeDuration();
    }

    @PostConstruct
    public void purgeExpiredDicomFiles() {
        List<DicomInstance> eligibleInstances = dicomInstanceRepository.findUnpurgedInstances();

        for (DicomInstance instance : eligibleInstances) {
            String rawStatus = instance.getProcessingStatus();

            WorkflowStatusEnum status;
            try {
                status = WorkflowStatusEnum.valueOf(rawStatus);
            } catch (IllegalArgumentException e) {
                System.out.println("Skipping unknown status: " + rawStatus);
                continue;
            }

            Duration expiry = purgeDurations.get(status);
            if (expiry == null) continue;

//            if (expiry.isZero()) {
//                deleteDicomFile(instance, "Immediate purge: " + status.name());
//                continue;
//            }

            Instant referenceTime = getRelevantTimestamp(status, instance);
            if (referenceTime == null) continue;

            Instant threshold = Instant.now().minus(expiry);
            if (referenceTime.isBefore(threshold)) {
                deleteDicomFile(instance, status.name() + " expired after " + expiry.toHours() + " hours");
            }
        }
    }

    private Instant getRelevantTimestamp(WorkflowStatusEnum status, DicomInstance instance) {
        return switch (status) {
            case DICOM_INSTANCE_RECEIVED, LIS_REQUEST_GENERATED, LIS_RESPONSE_RECEIVED ->
                    instance.getDicomInstanceReceivedTimestamp();
            case ENRICHMENT_STARTED, ENRICHMENT_COMPLETED, ENRICHMENT_FAILED ->
                    instance.getEnrichmentTimestamp();
        };
    }

    private void deleteDicomFile(DicomInstance instance, String reason) {
        String filePath = Paths.get(
                fileRootPath,
                instance.getActualStudyInstanceUid(),
                instance.getSeriesInstanceUid(),
                instance.getSopInstanceUid() + ".dcm"
        ).toString();

        try {
            boolean deleted = Files.deleteIfExists(Paths.get(filePath));
            if (deleted) {
                EnrichmentPurge purge = new EnrichmentPurge();
                purge.setSopInstanceUid(instance.getSopInstanceUid());
                purge.setDeletionDate(Instant.now());
                purge.setDeletionCriteria(reason);
                purge.setDicomInstanceReceivedTimeStamp(instance.getDicomInstanceReceivedTimestamp());
                purge.setDicomInstanceDeletedTimeStamp(Instant.now());

                enrichmentPurgeRepository.save(purge);

                System.out.println("Deleted: " + filePath + " | Reason: " + reason);
            } else {
                System.out.println("File not found: " + filePath);
            }

        } catch (IOException e) {
            System.err.println("Failed to delete: " + filePath);
            e.printStackTrace();
        }
    }
}
