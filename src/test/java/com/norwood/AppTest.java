package com.norwood;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.net.http.HttpRequest;

import com.norwood.util.HttpRequestSerializer;
import com.norwood.core.KatanaCore;
import com.norwood.core.KatanaResponse;

public class AppTest 
    extends TestCase
{
    public AppTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testSerializeUnserializeRoundtrip() {
        String line = "GET /hello HTTP/1.1";
        HttpRequest req = HttpRequestSerializer.unserialize(line);
        assertEquals("GET", req.method());
        assertEquals("/hello", req.uri().getPath());
        assertEquals(line, HttpRequestSerializer.serialize(req));
    }

    public void testUnserializeAllMethods() {
        String[] methods = {"GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS"};
        for (String method : methods) {
            String line = method + " /test HTTP/1.1";
            HttpRequest req = HttpRequestSerializer.unserialize(line);
            assertEquals(method, req.method());
            assertEquals(line, HttpRequestSerializer.serialize(req));
        }
    }

    public void testHandleRequestNotFound() {
        KatanaCore core = new KatanaCore();
        HttpRequest req = HttpRequestSerializer.unserialize("GET /missing HTTP/1.1");
        KatanaResponse res = core.handleRequest(req);
        assertEquals("Not Found", res.value());
    }
}
