package com.chekak.messenger.server;

import io.rsocket.AbstractRSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import reactor.core.publisher.Mono;

public class ServerMain {

    public static void main(String[] args) {
        RequestHandlerRouter router = new ReplyProcessorRequestHandlerRouter();
        RSocketFactory.receive()
                .acceptor((setup, sendingSocket) -> {
                    AbstractRSocket rSocket = new MessengerServerRSocket(router);
                    return Mono.just(rSocket);
                })
                .transport(TcpServerTransport.create("0.0.0.0", 8080))
                .start()
                .block()
                .onClose()
                .block();
    }
}
