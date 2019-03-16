package com.chekak.messenger.client;

import com.chekak.messenger.core.IMessageEmitter;
import com.chekak.messenger.protocol.MessageDto;
import java.io.InputStream;
import java.util.Scanner;

class UserMessageEmitter implements IMessageEmitter {

    private final Scanner scanner;

    UserMessageEmitter(InputStream source) {
        this.scanner = new Scanner(source);
    }

    @Override
    public MessageDto nextMessage() {
        return new MessageDto(scanner.nextLine());
    }
}
