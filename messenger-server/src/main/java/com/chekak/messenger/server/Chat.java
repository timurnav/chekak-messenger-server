package com.chekak.messenger.server;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Chat {

    private final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();

    public boolean containsUser(String name) {
        return clients.containsKey(name);
    }

    public void joinUser(String name, ClientHandler handler) {
        this.clients.put(name, handler);
        broadcast(name + " is joined", name);
    }

    public void broadcast(String message, String... exceptUsers) {
        Set<String> filter = new HashSet<>(Arrays.asList(exceptUsers));
        clients.forEach((name, client) -> {
            if (filter.contains(name) || client.isClosed()) {
                return;
            }
            client.sendMessage(message);
        });
    }

    public void leave(String name) {
        this.clients.remove(name);
        broadcast(name + " is left");
    }
}
