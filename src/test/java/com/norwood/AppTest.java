package com.norwood;

import com.norwood.userland.Scraper;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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

    public void testApp()
    {
        Scraper scraper = new Scraper();

        assertEquals(scraper.printStr(), "Test string");
    }
}
