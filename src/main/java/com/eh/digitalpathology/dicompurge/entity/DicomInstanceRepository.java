package com.eh.digitalpathology.dicompurge.entity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;

public interface DicomInstanceRepository extends MongoRepository<DicomInstance, String> {


    @Query("""
        SELECT d FROM DicomInstance d
        LEFT JOIN EnrichmentPurge e ON d.sopInstanceUid = e.sopInstanceUid
        WHERE e.sopInstanceUid IS NULL
    """)
    List<DicomInstance> findUnpurgedInstances();
}
