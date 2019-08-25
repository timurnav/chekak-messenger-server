package com.chekak.messenger.client;

import com.chekak.messenger.protocol.DataMessage;
import com.chekak.messenger.protocol.MessageConverter;
import com.chekak.messenger.protocol.MetaMessage;
import io.rsocket.Payload;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Stream;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class ClientMain {

    public static void main(String[] args) throws IOException {
        MessageConverter converter = new MessageConverter();
        Console console = new Console();
        String author = console.readLine("Who are you?");
        MessageProcessorFactory factory = new MessageProcessorFactory(console, converter, author);

        RSocketFactory
                .connect()
                .transport(TcpClientTransport.create(8080))
                .start()
                .flatMapMany(rSocket -> rSocket
                        .requestChannel(factory.getMessagePublisher())
                        .doOnNext(factory::consumeMessage)
                )
                .blockLast();
    }

    private static class MessageProcessorFactory {

        private final Console console;
        private final MessageConverter converter;
        private final String author;

        public MessageProcessorFactory(Console console, MessageConverter converter, String author) {
            this.console = console;
            this.converter = converter;
            this.author = author;
        }

        public Publisher<Payload> getMessagePublisher() {
            return Flux.concat(Flux.just("joined"), Flux.fromStream(Stream.generate(this::readNextLine)))
                    .map(msg -> {
                        MetaMessage meta = new MetaMessage();
                        meta.author = author;
                        DataMessage data = new DataMessage();
                        data.id = UUID.randomUUID().toString();
                        data.message = msg;
                        data.sent = System.currentTimeMillis();
                        return DefaultPayload.create(converter.serialize(data), converter.serialize(meta));
                    })
                    .subscribeOn(Schedulers.elastic());
        }

        private String readNextLine() {
            return console.readLine(author + ">");
        }

        public void consumeMessage(Payload payload) {
            MetaMessage meta = converter.deserialize(payload.getMetadataUtf8(), MetaMessage.class);
            DataMessage data = converter.deserialize(payload.getDataUtf8(), DataMessage.class);
            if (meta.author.equals(author)) {
                return;
            }
            console.printLine(meta.author, data.message);
        }
    }
}
