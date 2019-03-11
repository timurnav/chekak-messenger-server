package com.chekak.messenger.server;

import com.chekak.messenger.core.SocketManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final ServerSocket serverSocket;

    private final Chat chat = new Chat();

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        do {
            Socket socket = serverSocket.accept();
            SocketManager socketManager = new SocketManager(socket);
            String name = new NameChecker(socketManager).getCheckedName(chat);
            if (name == null) {
                continue;
            }
            ClientHandler handler = new ClientHandler(socketManager, name, chat);
            new Thread(handler).start();
            chat.joinUser(name, handler);
        } while (true);
    }
}
