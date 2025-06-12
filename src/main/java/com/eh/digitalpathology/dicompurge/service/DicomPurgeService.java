package com.eh.digitalpathology.dicompurge.service;

import com.eh.digitalpathology.dicompurge.entity.DicomInstance;
import com.eh.digitalpathology.dicompurge.entity.DicomInstanceRepository;
import com.eh.digitalpathology.dicompurge.entity.EnrichmentPurge;
import com.eh.digitalpathology.dicompurge.entity.EnrichmentPurgeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class DicomPurgeService {

    @Value("${dicom.expiry.days}")
    private int expiryDays;

    @Value("${optfile.server.path}")
    private String fileRootPath;

    private final DicomInstanceRepository dicomInstanceRepository;
    private final EnrichmentPurgeRepository enrichmentPurgeRepository;

    @Autowired
    public DicomPurgeService(DicomInstanceRepository dicomInstanceRepository,
                              EnrichmentPurgeRepository enrichmentPurgeRepository) {
        this.dicomInstanceRepository = dicomInstanceRepository;
        this.enrichmentPurgeRepository = enrichmentPurgeRepository;
    }

    @PostConstruct  // only for testing, remove this after testing.
    public void purgeExpiredDicomFiles() {
        Instant expiryThreshold = Instant.now().minus(Duration.ofDays(expiryDays));
        List<DicomInstance> expiredInstances = dicomInstanceRepository
                .findByDicomInstanceReceivedTimestampBeforeOrEnrichmentTimestampBefore(
                        expiryThreshold, expiryThreshold
                );

        for (DicomInstance instance : expiredInstances) {
            String filePath = Paths.get(
                    fileRootPath,
                    instance.getActualStudyInstanceUid(),
                    instance.getSeriesInstanceUid(),
                    instance.getSopInstanceUid() + ".dcm"
            ).toString();

            try {
                Files.deleteIfExists(Paths.get(filePath));

                EnrichmentPurge purge = new EnrichmentPurge();
                purge.setSopInstanceUid(instance.getSopInstanceUid());
                purge.setDeletionDate(Instant.now());
                purge.setDeletionCriteria(getDeletionCriteria(expiryThreshold, instance));

                enrichmentPurgeRepository.save(purge);

                System.out.println("Deleted file: " + filePath);

            } catch (IOException e) {
                System.err.println("Failed to delete file: " + filePath);
                e.printStackTrace();
            }
        }
    }

    private String getDeletionCriteria(Instant expiryThreshold, DicomInstance instance) {
        boolean isReceivedExpired = instance.getDicomInstanceReceivedTimestamp() != null &&
                instance.getDicomInstanceReceivedTimestamp().isBefore(expiryThreshold);

        boolean isEnrichmentExpired = instance.getEnrichmentTimestamp() != null &&
                instance.getEnrichmentTimestamp().isBefore(expiryThreshold);

        // Build deletion criteria based on which timestamps are expired
        String deletionCriteria = null;
        if (isReceivedExpired && isEnrichmentExpired) {
            deletionCriteria = "Received and Enrichment Dates older than " + expiryDays + " days";
        } else if (isReceivedExpired) {
            deletionCriteria = "Received Date older than " + expiryDays + " days";
        } else if (isEnrichmentExpired) {
            deletionCriteria = "Enrichment Date older than " + expiryDays + " days";
        }
        return deletionCriteria;
    }
}
