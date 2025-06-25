package com.eh.digitalpathology.dicompurge.entity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrichmentPurgeRepository extends MongoRepository<EnrichmentPurge, String> {

    @Query("SELECT e.sopInstanceUid FROM EnrichmentPurge e")
    List<String> findAllSopInstanceUids();

}