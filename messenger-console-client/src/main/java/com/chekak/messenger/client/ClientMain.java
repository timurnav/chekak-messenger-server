package com.chekak.messenger.client;

import java.net.InetAddress;

public class ClientMain {

    public static void main(String[] args) {
        try {
            ConsoleClient consoleClient = new ConsoleClient(InetAddress.getByName("localhost"), 1234);
            consoleClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
