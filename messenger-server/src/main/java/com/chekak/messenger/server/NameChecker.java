package com.chekak.messenger.server;

import com.chekak.messenger.core.SocketManager;

public class NameChecker {

    private final SocketManager socketManager;

    public NameChecker(SocketManager socketManager) {
        this.socketManager = socketManager;
    }

    public String getCheckedName(Chat chat) {
        try {
            String name = socketManager.nextSocketMessage().trim();
            if (name.contains(" ")) {
                socketManager.sendSocketMessage("Name can't contain spaces");
                return getCheckedName(chat);
            } else if (chat.containsUser(name)) {
                socketManager.sendSocketMessage("User with same name is already added");
                return getCheckedName(chat);
            } else {
                socketManager.sendSocketMessage("OK");
                return name;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
