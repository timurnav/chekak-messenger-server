package com.chekak.messenger.server;

import com.chekak.messenger.core.SocketManager;
import com.chekak.messenger.protocol.MessageDto;

public class NameChecker {

    private final SocketManager socketManager;

    public NameChecker(SocketManager socketManager) {
        this.socketManager = socketManager;
    }

    public String getCheckedName(Chat chat) {
        try {
            String name = socketManager.nextSocketMessage().getMessage().trim();
            if (name.contains(" ")) {
                socketManager.sendSocketMessage(new MessageDto("Name can't contain spaces"));
                return getCheckedName(chat);
            } else if (chat.containsUser(name)) {
                socketManager.sendSocketMessage(new MessageDto("User with same name is already added"));
                return getCheckedName(chat);
            } else {
                socketManager.sendSocketMessage(new MessageDto("OK"));
                return name;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
