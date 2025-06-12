package com.eh.digitalpathology.dicompurge.entity;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DicomInstanceRepository extends MongoRepository<DicomInstance, String> {
    List<DicomInstance> findByBarcodeIsNull();
}
