package com.norwood.core;

import com.norwood.networking.KatanaServer;
import com.norwood.util.KatanaClient;

public class KatanaCore
{
    public void boot() {
        System.out.println("Booting Katana");
        (new Thread(() -> KatanaServer.getInstance())).start();
        System.out.println("Katana booted. Awaiting connections form clients...");

        KatanaClient katanaClient = new KatanaClient();
        katanaClient.sendRequest();
    }
}
