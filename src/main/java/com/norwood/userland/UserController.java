package com.norwood.userland;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import com.norwood.core.Singleton;
import com.norwood.core.annotations.Inject;
import com.norwood.routing.annotation.Get;
import com.norwood.routing.annotation.Post;
import com.norwood.pipeline.fhir.FhirIngestionService;
import com.norwood.pipeline.fhir.PatientRecord;

@Singleton
public class UserController {
    @Inject UserService userService;
    @Inject Scraper scraper;
    @Inject FhirIngestionService fhirService;

    @Post(path = "/test1")
    public int route1(HttpRequest request) {
        System.out.println("[UserController] /test1 executed :" + request.method());
        return 1;
    }

    @Get(path = "/scrape")
    public void scrape(HttpRequest request) {
        scraper.scrape();
    }

    @Get(path = "/test3")
    public String userServiceTest(HttpRequest request) {
        return userService.userMethod();
    }

    @Post(path = "/fhir-ingest")
    public String fhirIngest(HttpRequest request) {
        String query = request.uri().getQuery();
        if (query != null && query.startsWith("file=")) {
            Path file = Path.of(query.substring("file=".length()));
            PatientRecord pr = fhirService.ingestFromFile(file);
            return pr.toString();
        }
        PatientRecord pr = fhirService.ingestFromString("{\"id\":\"demo\"}");
        return pr.toString();
    }

    @Get(path = "/test2")
    public String route2(HttpRequest request) {
        System.out.println("[UserController] /test2 executed :" + request.method());
        try {
            return Files.readString(Path.of("resources/index.html"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to return index.html");
        }
    }

    @Get(path = "/users/{id}")
    public String routeUser(String id, HttpRequest request) {
        return "user:" + id;
    }
}
