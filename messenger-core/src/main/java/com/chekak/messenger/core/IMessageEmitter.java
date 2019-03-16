package com.chekak.messenger.core;

import com.chekak.messenger.protocol.MessageDto;

public interface IMessageEmitter {

    MessageDto nextMessage();
}
