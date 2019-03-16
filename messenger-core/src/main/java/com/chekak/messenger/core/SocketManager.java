package com.chekak.messenger.core;

import com.chekak.messenger.protocol.MessageDto;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class SocketManager implements IMessageConsumer, IMessageEmitter {

    private final XmlMapper xmlMapper = new XmlMapper();

    private final Socket socket;
    private final DataInput is;
    private final DataOutput os;
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
    public void consumeMessage(MessageDto message) {
        sendSocketMessage(message);
    }

    @Override
    public MessageDto nextMessage() {
        return nextSocketMessage();
    }

    public void sendSocketMessage(MessageDto message) {
        if (isClosed) {
            throw new CannotSendMessageException("Connection was closed");
        }
        try {
            String rawMessage = xmlMapper.writeValueAsString(message);
            os.writeUTF(rawMessage);
        } catch (IOException e) {
            terminate();
            throw new CannotSendMessageException(e);
        }
    }

    public MessageDto nextSocketMessage() {
        if (isClosed) {
            throw new CannotReceiveMessageException("Connection was closed");
        }
        try {
            String rawMessage = is.readUTF();
            return xmlMapper.readValue(rawMessage, MessageDto.class);
        } catch (IOException e) {
            terminate();
            throw new CannotReceiveMessageException(e);
        }
    }

    public void terminate() {
        isClosed = true;
        try {
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
