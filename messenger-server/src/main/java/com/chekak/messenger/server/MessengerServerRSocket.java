package com.chekak.messenger.server;

import static com.chekak.messenger.protocol.MessageConverter.deserialize;
import static com.chekak.messenger.protocol.MessageConverter.serialize;

import com.chekak.messenger.protocol.Message;
import com.chekak.messenger.protocol.RequestMeta;
import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MessengerServerRSocket extends AbstractRSocket {

    private final RequestHandlerRouter router;

    public MessengerServerRSocket(RequestHandlerRouter router) {
        this.router = router;
    }

//    @Override
//    public Mono<Payload> requestResponse(Payload payload) {
//        try {
//            RequestMeta meta = deserialize(payload.getMetadataUtf8(), RequestMeta.class);
//            Message response = router.handle(meta, payload);
//            return Mono.just(DefaultPayload.create(serialize(response), payload.getMetadataUtf8()));
//        } catch (Exception e) {
//            return Mono.error(e);
//        }
//    }
//
//    @Override
//    public Flux<Payload> requestStream(Payload payload) {
//        try {
//            RequestMeta meta = deserialize(payload.getMetadataUtf8(), RequestMeta.class);
//            return router.handleStream(meta, payload)
//                    .map(message -> DefaultPayload.create(
//                            serialize(message),
//                            payload.getMetadataUtf8()
//                    ));
//        } catch (Exception e) {
//            return Flux.error(e);
//        }
//    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        Flux.from(payloads).subscribe(router::handleMessage);
        return router.messagePublisher();
    }
}
