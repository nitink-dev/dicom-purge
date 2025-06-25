package com.eh.digitalpathology.dicompurge.service;

public enum WorkflowStatusEnum {
    DICOM_INSTANCE_RECEIVED("dicom-receiver"),
    LIS_REQUEST_GENERATED("message-generator"),
    LIS_RESPONSE_RECEIVED("lis-connector"),
    ENRICHMENT_STARTED("dicom-enriched"),
    ENRICHMENT_COMPLETED("dicom-enriched-completed"),
    ENRICHMENT_FAILED("dicom-enriched-failed");

    private final String source;

    WorkflowStatusEnum(String source) {
        this.source = source;
    }

    public String getValue() {
        return source;
    }
}

