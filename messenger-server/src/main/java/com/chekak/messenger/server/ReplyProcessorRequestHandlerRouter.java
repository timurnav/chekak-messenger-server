package com.chekak.messenger.server;

import com.chekak.messenger.protocol.DataMessage;
import com.chekak.messenger.protocol.MessageConverter;
import com.chekak.messenger.protocol.MetaMessage;
import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.ReplayProcessor;

public class ReplyProcessorRequestHandlerRouter implements RequestHandlerRouter {

    private static final Logger log = LoggerFactory.getLogger(ReplyProcessorRequestHandlerRouter.class);

    private final ReplayProcessor<Payload> replyProcessor = ReplayProcessor.create();
    private final Flux<Payload> flux = replyProcessor.replay(10).autoConnect();
    private final FluxSink<Payload> sink = replyProcessor.sink();

    @Override
    public void handleMessage(Payload payload) {
        MetaMessage meta = MessageConverter.deserialize(payload.getMetadataUtf8(), MetaMessage.class);
        DataMessage data = MessageConverter.deserialize(payload.getDataUtf8(), DataMessage.class);
        log.info(Instant.ofEpochMilli(data.sent) + ":" + meta.author + ":" + data.message);
        sink.next(payload);
    }

    @Override
    public Flux<Payload> messagePublisher() {
        return flux.map(DefaultPayload::create);
    }
}
