package com.norwood;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.net.http.HttpRequest;

import com.norwood.util.HttpRequestSerializer;

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
}
