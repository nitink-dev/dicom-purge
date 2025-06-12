package com.eh.digitalpathology.dicompurge.entity;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface DicomInstanceRepository extends MongoRepository<DicomInstance, String> {
    List<DicomInstance> findByDicomInstanceReceivedTimestampBeforeOrEnrichmentTimestampBefore(Instant receivedBefore, Instant enrichedBefore);
}
