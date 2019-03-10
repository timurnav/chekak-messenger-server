package com.chekak.messenger.client;

import com.chekak.messenger.core.IMessageConsumer;
import com.chekak.messenger.core.IMessageEmitter;
import com.chekak.messenger.core.MessageTransmitter;
import com.chekak.messenger.core.SocketManager;
import java.net.InetAddress;

public class ConsoleClient {

    private final InetAddress ip;
    private final int port;

    public ConsoleClient(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start() throws Exception {
        SocketManager socketManager = new SocketManager(ip, port);

        IMessageEmitter userMessageEmitter = new UserMessageEmitter(System.in);

        IMessageConsumer messageViewer = new MessageViewer(System.out);

        MessageTransmitter userMessageTransmitter = new MessageTransmitter(userMessageEmitter, socketManager);
        MessageTransmitter serverMessageTransmitter = new MessageTransmitter(socketManager, messageViewer);

        Thread userThread = createMessageTransmittingThread(userMessageTransmitter, socketManager);
        Thread serverThread = createMessageTransmittingThread(serverMessageTransmitter, socketManager);

        userThread.start();
        serverThread.start();
    }

    private Thread createMessageTransmittingThread(MessageTransmitter messageTransmitter, SocketManager socketManager) {
        return new Thread(() -> {
            messageTransmitter.transmitMessages();
            socketManager.terminate();
            System.out.println("Server connection lost");
            System.exit(0);
        });
    }
}
