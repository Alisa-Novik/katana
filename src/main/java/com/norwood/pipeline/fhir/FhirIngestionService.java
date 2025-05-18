package com.norwood.pipeline.fhir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Service responsible for ingesting a FHIR Bundle provided as a JSON string or
 * file. For simplicity this implementation uses a stubbed parser to extract
 * minimal information and transform it into a {@link PatientRecord}.
 */
public class FhirIngestionService {

    public PatientRecord ingestFromString(String bundleJson) {
        FhirBundle bundle = parseBundle(bundleJson);
        return transform(bundle);
    }

    public PatientRecord ingestFromFile(Path file) {
        try {
            String json = Files.readString(file);
            return ingestFromString(json);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read bundle file", e);
        }
    }

    // ---------------------------------------------------------------------
    // Internal helper types and methods
    // ---------------------------------------------------------------------

    private PatientRecord transform(FhirBundle bundle) {
        return new PatientRecord(bundle.id, bundle.rawJson);
    }

    /**
     * Stubbed parser that attempts to extract a bundle id from the JSON string.
     * In a real implementation this would leverage a library such as HAPI FHIR.
     */
    private FhirBundle parseBundle(String json) {
        String id = extractId(json);
        return new FhirBundle(id, json);
    }

    private String extractId(String json) {
        try {
            int idx = json.indexOf("\"id\"");
            if (idx >= 0) {
                int colon = json.indexOf(':', idx);
                int start = json.indexOf('"', colon + 1);
                int end = json.indexOf('"', start + 1);
                if (start > -1 && end > -1) {
                    return json.substring(start + 1, end);
                }
            }
        } catch (Exception e) {
            // ignore and fall through to default
        }
        return "unknown";
    }

    // simple container for parsed bundle fields
    private static class FhirBundle {
        final String id;
        final String rawJson;

        FhirBundle(String id, String rawJson) {
            this.id = id;
            this.rawJson = rawJson;
        }
    }
}
