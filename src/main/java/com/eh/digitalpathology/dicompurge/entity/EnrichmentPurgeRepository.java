package com.eh.digitalpathology.dicompurge.entity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrichmentPurgeRepository extends MongoRepository<EnrichmentPurge, String> {}