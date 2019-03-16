package com.chekak.messenger.client;

import com.chekak.messenger.core.SocketManager;
import com.chekak.messenger.protocol.MessageDto;
import java.io.InputStream;
import java.util.Scanner;

public class ClientNameDealer {

    private final SocketManager socketManager;

    public ClientNameDealer(SocketManager socketManager) {
        this.socketManager = socketManager;
    }

    public void deal(InputStream is) {
        Scanner scanner = new Scanner(is);
        System.out.println("Please enter your name");
        String name = scanner.nextLine();

        checkName(name, scanner);

    }

    private void checkName(String name, Scanner scanner) {
        socketManager.sendSocketMessage(new MessageDto(name));
        String reply = socketManager.nextSocketMessage().getMessage();
        if ("OK".equals(reply)) {
            return;
        }
        System.out.println("Server didn't apply the name, due to " + reply);
        System.out.println("Please choose another name");
        String newName = scanner.nextLine();
        checkName(newName, scanner);
    }
}
