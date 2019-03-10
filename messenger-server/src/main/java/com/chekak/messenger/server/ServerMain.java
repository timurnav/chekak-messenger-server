package com.chekak.messenger.server;

import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) throws IOException {
        Server server = new Server(1234);
        server.start();
    }
}
