package com.chekak.messenger.core;

import com.chekak.messenger.protocol.MessageDto;

public interface IMessageConsumer {

    void consumeMessage(MessageDto message);
}
