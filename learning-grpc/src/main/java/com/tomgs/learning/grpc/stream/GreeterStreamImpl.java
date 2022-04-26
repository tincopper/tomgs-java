package com.tomgs.learning.grpc.stream;

import com.tomgs.learning.grpc.proto.GreeterStreamGrpc;
import com.tomgs.learning.grpc.proto.HelloStreamReply;
import com.tomgs.learning.grpc.proto.HelloStreamRequest;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

/**
 * GreeterStreamImpl
 *
 * @author tomgs
 * @since 2022/4/25
 */
public class GreeterStreamImpl extends GreeterStreamGrpc.GreeterStreamImplBase {

    private static final Logger logger = Logger.getLogger(GreeterStreamImpl.class.getName());

    @Override
    public StreamObserver<HelloStreamRequest> sayHelloStream(StreamObserver<HelloStreamReply> responseObserver) {

        return new StreamObserver<HelloStreamRequest>() {

            @Override
            public void onNext(HelloStreamRequest value) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        };
    }

}
