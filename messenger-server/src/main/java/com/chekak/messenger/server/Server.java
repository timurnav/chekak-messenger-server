package com.chekak.messenger.server;

import static java.util.Collections.unmodifiableMap;

import com.chekak.messenger.core.IMessageConsumer;
import com.chekak.messenger.core.MessageTransmitter;
import com.chekak.messenger.core.SocketManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private final ServerSocket serverSocket;

    private final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();

    private static int i = 0;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        do {
            Socket socket = serverSocket.accept();
            String name = "client " + i++;
            ClientHandler handler = new ClientHandler(socket, name, unmodifiableMap(clients));
            new Thread(handler).start();
            clients.put(name, handler);
        } while (true);

    }

    private static class UserMessageSender implements IMessageConsumer {

        private final String currentUserName;
        private final Map<String, ClientHandler> clients;

        public UserMessageSender(String currentUserName, Map<String, ClientHandler> clients) {
            this.currentUserName = currentUserName;
            this.clients = clients;
        }

        @Override
        public void consumeMessage(String message) {
            String trimmed = message.trim();
            if (trimmed.indexOf("#") == 0) {
                if (trimmed.indexOf(" ", 1) > 0) {

                }
            } else {
                clients.forEach((name, client) -> {
                    if (!currentUserName.equals(name) && !client.isClosed()) {
                        client.sendMessage(currentUserName + " : " + message);
                    }
                });
            }
        }
    }

    private static class ClientHandler implements Runnable {

        private final SocketManager socketManager;
        private final MessageTransmitter messageTransmitter;

        public ClientHandler(Socket socket, String name, Map<String, ClientHandler> clients) throws IOException {
            this.socketManager = new SocketManager(socket);
            this.messageTransmitter = new MessageTransmitter(socketManager, new UserMessageSender(name, clients));
        }

        public boolean isClosed() {
            return socketManager.isClosed();
        }

        public void sendMessage(String message) {
            socketManager.sendSocketMessage(message);
        }

        @Override
        public void run() {
            messageTransmitter.transmitMessages();
            socketManager.terminate();
        }
    }

}
