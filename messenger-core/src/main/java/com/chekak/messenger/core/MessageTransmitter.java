package com.chekak.messenger.core;

import com.chekak.messenger.protocol.MessageDto;

public class MessageTransmitter {

    private final IMessageEmitter messageEmitter;
    private final IMessageConsumer messageConsumer;

    public MessageTransmitter(IMessageEmitter messageEmitter, IMessageConsumer messageConsumer) {
        this.messageEmitter = messageEmitter;
        this.messageConsumer = messageConsumer;
    }

    public void transmitMessages() {
        while (true) {
            try {
                MessageDto message = messageEmitter.nextMessage();
                messageConsumer.consumeMessage(message);
            } catch (Exception e) {
                throw new MessageTransmittingException(e);
            }
        }
    }

    public static class MessageTransmittingException extends RuntimeException {

        public MessageTransmittingException(Throwable cause) {
            super(cause);
        }
    }

}
