package com.chekak.messenger.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class SocketManager implements IMessageConsumer, IMessageEmitter {

    private final Socket socket;
    private final DataInputStream is;
    private final DataOutputStream os;
    private volatile boolean isClosed;

    public SocketManager(InetAddress ip, int port) throws IOException {
        this(new Socket(ip, port));
    }

    public SocketManager(Socket socket) throws IOException {
        this.socket = socket;
        this.is = new DataInputStream(socket.getInputStream());
        this.os = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void consumeMessage(String message) {
        sendSocketMessage(message);
    }

    @Override
    public String nextMessage() {
        return nextSocketMessage();
    }

    public void sendSocketMessage(String message) {
        if (isClosed) {
            throw new CannotSendMessageException("Connection was closed");
        }
        try {
            os.writeUTF(message);
        } catch (IOException e) {
            terminate();
            throw new CannotSendMessageException(e);
        }
    }

    public String nextSocketMessage() {
        if (isClosed) {
            throw new CannotReceiveMessageException("Connection was closed");
        }
        try {
            return is.readUTF();
        } catch (IOException e) {
            terminate();
            throw new CannotReceiveMessageException(e);
        }
    }

    public void terminate() {
        isClosed = true;
        try {
            is.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isClosed() {
        return isClosed;
    }

    private static class CannotSendMessageException extends RuntimeException {

        public CannotSendMessageException(String message) {
            super(message);
        }

        public CannotSendMessageException(Throwable cause) {
            super(cause);
        }
    }

    private static class CannotReceiveMessageException extends RuntimeException {

        public CannotReceiveMessageException(String message) {
            super(message);
        }

        public CannotReceiveMessageException(Throwable cause) {
            super(cause);
        }
    }
}
