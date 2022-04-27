package com.tomgs.learning.grpc.stream;

import com.tomgs.learning.grpc.core.AbstractConnection;
import com.tomgs.learning.grpc.core.Event;
import com.tomgs.learning.grpc.proto.GreeterStreamGrpc;
import com.tomgs.learning.grpc.proto.HelloStreamReply;
import com.tomgs.learning.grpc.proto.HelloStreamRequest;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GreeterStreamImpl
 *
 * @author tomgs
 * @since 2022/4/25
 */
@Slf4j
public class GreeterStreamImpl extends GreeterStreamGrpc.GreeterStreamImplBase {

    private final Map<String, AbstractConnection<HelloStreamReply>> connections = new ConcurrentHashMap<>(16);

    @Override
    public StreamObserver<HelloStreamRequest> sayHelloStream(StreamObserver<HelloStreamReply> responseObserver) {

        AbstractConnection<HelloStreamReply> newConnection = new GreeterStreamConnection(responseObserver);

        return new StreamObserver<HelloStreamRequest>() {
            private  boolean initRequest = true;

            @Override
            public void onNext(HelloStreamRequest request) {
                // init request
                if (initRequest) {
                    newConnection.setConnectionId(request.getName());
                    connections.put(newConnection.getConnectionId(), newConnection);
                    initRequest = false;
                }

                // do something
                HelloStreamReply reply = HelloStreamReply.newBuilder().setMessage("hello:" + request.getName()).build();
                newConnection.push(reply, null);
            }

            @Override
            public void onError(Throwable t) {
                log.error("error: {}", t.getMessage(), t);
                clear();
            }

            @Override
            public void onCompleted() {
                //clear();
                //log.info("greeter stream: {} stream close.", newConnection.getConnectionId());
                //responseObserver.onCompleted();
                //clear();
            }

            private void clear() {
                connections.remove(newConnection.getConnectionId());
            }
        };
    }

    public boolean hasClientConnection() {
        return connections.size() > 0;
    }

    public void handleEvent(Event event) {
        HelloStreamReply reply = HelloStreamReply.newBuilder().setMessage("hello:" + event.getType()).build();
        for (AbstractConnection<HelloStreamReply> connection : connections.values()) {
            try {
                connection.push(reply, null);
            } catch (Exception e) {
                connections.remove(connection.getConnectionId());
            }
        }
    }

}
