package com.chekak.messenger.client;

import com.chekak.messenger.core.IMessageEmitter;
import java.io.InputStream;
import java.util.Scanner;

class UserMessageEmitter implements IMessageEmitter {

    private final Scanner scanner;

    UserMessageEmitter(InputStream source) {
        this.scanner = new Scanner(source);
    }

    @Override
    public String nextMessage() {
        return scanner.nextLine();
    }
}
