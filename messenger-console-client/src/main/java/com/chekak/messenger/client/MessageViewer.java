package com.chekak.messenger.client;

import com.chekak.messenger.core.IMessageConsumer;
import java.io.PrintStream;

class MessageViewer implements IMessageConsumer {

    private final PrintStream out;

    public MessageViewer(PrintStream out) {
        this.out = out;
    }

    @Override
    public void consumeMessage(String message) {
        out.println(message);
    }
}
