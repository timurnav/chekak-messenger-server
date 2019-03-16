package com.chekak.messenger.server;

import com.chekak.messenger.core.IMessageConsumer;
import com.chekak.messenger.protocol.MessageDto;

class UserMessageSender implements IMessageConsumer {

    private final String currentUserName;
    private final Chat chat;
//    private final Map<String, ClientHandler> clients;

    public UserMessageSender(String currentUserName, Chat chat) {
        this.currentUserName = currentUserName;
        this.chat = chat;
    }

    @Override
    public void consumeMessage(MessageDto message) {
        chat.broadcast(currentUserName + " : " + message.getMessage(), currentUserName);
    }
}
