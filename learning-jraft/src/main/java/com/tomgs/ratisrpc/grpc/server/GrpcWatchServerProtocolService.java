package com.tomgs.ratisrpc.grpc.server;

import cn.hutool.core.util.StrUtil;
import com.tomgs.learning.grpc.proto.*;
import com.tomgs.ratisrpc.grpc.core.AbstractConnection;
import com.tomgs.ratisrpc.grpc.core.WatchManager;
import com.tomgs.ratisrpc.grpc.core.WatchedStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.ratis.thirdparty.io.grpc.stub.StreamObserver;

/**
 * GrpcWatchServerProtocolService
 *
 * @author tomgs
 * @since 1.0
 */
@Slf4j
public class GrpcWatchServerProtocolService extends WatchServiceGrpc.WatchServiceImplBase {

    private final WatchManager watchManager = WatchManager.INSTANCE;

    @Override
    public StreamObserver<WatchRequest> watch(StreamObserver<WatchResponse> responseObserver) {
        // new connection
        WatchStreamConnection watchStreamConnection = new WatchStreamConnection(responseObserver);

        return new StreamObserver<WatchRequest>() {
            private  boolean initRequest = true;

            @Override
            public void onNext(WatchRequest request) {
                // init request
                if (initRequest) {
                    watchStreamConnection.setConnectionId(StrUtil.toString(request.getNodeId()));
                    watchManager.addWatchConnection(watchStreamConnection.getConnectionId(), watchStreamConnection);
                    initRequest = false;
                }

                if (request.hasCreateRequest()) {
                    final WatchCreateRequest createRequest = request.getCreateRequest();
                    watchManager.addWatchRequest(createRequest);
                }
                if (request.hasCancelRequest()) {
                    final WatchCancelRequest cancelRequest = request.getCancelRequest();
                    watchManager.cancelWatchRequest(cancelRequest);
                }
            }

            @Override
            public void onError(Throwable t) {
                log.error("error: {}", t.getMessage(), t);
                clear();
            }

            @Override
            public void onCompleted() {
                log.info("watch stream: {} stream close.", watchStreamConnection.getConnectionId());
                responseObserver.onCompleted();
                clear();
            }

            private void clear() {
                watchManager.removeWatchConnection(watchStreamConnection.getConnectionId());
            }
        };
    }

    static class WatchStreamConnection extends AbstractConnection<WatchResponse> {

        public WatchStreamConnection(StreamObserver<WatchResponse> streamObserver) {
            super(streamObserver);
        }

        @Override
        public void push(WatchResponse response, WatchedStatus watchedStatus) {
            if (log.isDebugEnabled()) {
                log.debug("WatchResponse: {}", response.toString());
            }

            this.streamObserver.onNext(response);

            // Update watched status
            //watchedStatus.setLatestVersion(response.getSystemVersionInfo());
            //watchedStatus.setLatestNonce(response.getNonce());

            //log.info("watch: push, type: {}, connection-id {}, version {}, nonce {}, resource size {}.",
            //        watchedStatus.getType(),
            //        getConnectionId(),
            //        response.getSystemVersionInfo(),
            //        response.getNonce(),
            //        response.getResourcesCount());
        }

    }

}
