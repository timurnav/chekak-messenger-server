package com.chekak.messenger.server;

import com.chekak.messenger.protocol.Message;
import com.chekak.messenger.protocol.RequestHandler;
import com.chekak.messenger.protocol.RequestMeta;
import io.rsocket.Payload;
import reactor.core.publisher.Flux;

public interface RequestHandlerRouter {

//    Message handle(RequestMeta meta, Payload payload);
//
//    Flux<Message> handleStream(RequestMeta meta, Payload payload);

    void handleMessage(Payload payload);

    Flux<Payload> messagePublisher();
}
