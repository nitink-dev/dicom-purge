package com.eh.digitalpathology.dicompurge.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "enrichment_purge")
public class EnrichmentPurge {
    @Id
    private String id;

    private String sopInstanceUid;
    private Instant deletionDate;
    private String deletionCriteria;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSopInstanceUid() {
        return sopInstanceUid;
    }

    public void setSopInstanceUid(String sopInstanceUid) {
        this.sopInstanceUid = sopInstanceUid;
    }

    public Instant getDeletionDate() {
        return deletionDate;
    }

    public void setDeletionDate(Instant deletionDate) {
        this.deletionDate = deletionDate;
    }

    public String getDeletionCriteria() {
        return deletionCriteria;
    }

    public void setDeletionCriteria(String deletionCriteria) {
        this.deletionCriteria = deletionCriteria;
    }
}
