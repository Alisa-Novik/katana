package com.norwood.pipeline.fhir;

/**
 * Simple model representing a patient record derived from a FHIR Bundle.
 */
public class PatientRecord {
    private final String id;
    private final String source;

    public PatientRecord(String id, String source) {
        this.id = id;
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "PatientRecord{" +
            "id='" + id + '\'' +
            ", source='" + source + '\'' +
            '}';
    }
}
