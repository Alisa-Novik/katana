package com.norwood.pipeline.fhir;

import junit.framework.TestCase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FhirIngestionServiceTest extends TestCase {

    public void testIngestFromString() {
        FhirIngestionService svc = new FhirIngestionService();
        String json = "{\"resourceType\":\"Bundle\",\"id\":\"patient-1\"}";
        PatientRecord rec = svc.ingestFromString(json);
        assertEquals("patient-1", rec.getId());
        assertEquals(json, rec.getSource());
    }

    public void testIngestFromFile() throws IOException {
        FhirIngestionService svc = new FhirIngestionService();
        String json = "{\"resourceType\":\"Bundle\",\"id\":\"file-1\"}";
        Path temp = Files.createTempFile("bundle", ".json");
        Files.writeString(temp, json);
        try {
            PatientRecord rec = svc.ingestFromFile(temp);
            assertEquals("file-1", rec.getId());
            assertEquals(json, rec.getSource());
        } finally {
            Files.deleteIfExists(temp);
        }
    }
}
