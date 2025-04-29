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
        
        // test 1
        
        return new TestSuite( AppTest.class );
    }
}
