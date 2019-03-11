package com.chekak.messenger.server;

import com.chekak.messenger.core.MessageTransmitter;
import com.chekak.messenger.core.SocketManager;

public class ClientHandler implements Runnable {

    private final SocketManager socketManager;
    private final String name;
    private final Chat chat;
    private final MessageTransmitter messageTransmitter;

    public ClientHandler(SocketManager socketManager, String name, Chat chat) {
        this.socketManager = socketManager;
        this.name = name;
        this.chat = chat;
        this.messageTransmitter = new MessageTransmitter(this.socketManager, new UserMessageSender(name, chat));
    }

    public boolean isClosed() {
        return socketManager.isClosed();
    }

    public void sendMessage(String message) {
        socketManager.sendSocketMessage(message);
    }

    @Override
    public void run() {
        try {
            messageTransmitter.transmitMessages();
        } catch (Exception e) {
            chat.leave(name);
            socketManager.terminate();
        }
    }
}
