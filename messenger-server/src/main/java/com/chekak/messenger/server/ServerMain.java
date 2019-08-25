package com.chekak.messenger.server;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ReplayProcessor;

public class ServerMain {

    public static void main(String[] args) {
        ReplayProcessor<Payload> replyProcessor = ReplayProcessor.create();
        Flux<Payload> flux = replyProcessor.replay(10).autoConnect();
        FluxSink<Payload> sink = replyProcessor.sink();

        RSocketFactory.receive()
                .acceptor((setup, sendingSocket) -> {
                    AbstractRSocket rSocket = new AbstractRSocket() {
                        @Override
                        public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
                            Flux.from(payloads).subscribe(sink::next);
                            return flux.map(DefaultPayload::create);
                        }
                    };
                    return Mono.just(rSocket);
                })
                .transport(TcpServerTransport.create("0.0.0.0", 8080))
                .start()
                .block()
                .onClose()
                .block();
    }
}
