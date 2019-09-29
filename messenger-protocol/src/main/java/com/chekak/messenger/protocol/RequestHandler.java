package com.chekak.messenger.protocol;

public interface RequestHandler<T extends Message> {

    Object handle(T request);

    Class<T> getType();
}
