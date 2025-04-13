package com.norwood;

import com.norwood.core.KatanaCore;
import com.norwood.util.KatanaClient;

public class App 
{
    public static void main(String[] args)
    {
        KatanaCore core = new KatanaCore();
        core.boot();

        // KatanaClient katanaClient = new KatanaClient();
        // katanaClient.sendRequest(katanaClient.createRequest("/test2"));
        // katanaClient.sendRequest(katanaClient.createRequest("/test2"));
    }
}
