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
    private Instant dicomInstanceReceivedTimeStamp;
    private Instant dicomInstanceDeletedTimeStamp;


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

    public Instant getDicomInstanceReceivedTimeStamp() {
        return dicomInstanceReceivedTimeStamp;
    }

    public void setDicomInstanceReceivedTimeStamp(Instant dicomInstanceReceivedTimeStamp) {
        this.dicomInstanceReceivedTimeStamp = dicomInstanceReceivedTimeStamp;
    }

    public Instant getDicomInstanceDeletedTimeStamp() {
        return dicomInstanceDeletedTimeStamp;
    }

    public void setDicomInstanceDeletedTimeStamp(Instant dicomInstanceDeletedTimeStamp) {
        this.dicomInstanceDeletedTimeStamp = dicomInstanceDeletedTimeStamp;
    }
}
