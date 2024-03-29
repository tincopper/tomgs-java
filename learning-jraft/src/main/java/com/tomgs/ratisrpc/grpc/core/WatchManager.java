package com.tomgs.ratisrpc.grpc.core;

import cn.hutool.core.thread.ThreadUtil;
import com.tomgs.common.BytesUtil;
import com.tomgs.learning.grpc.proto.DataChangeEvent;
import com.tomgs.learning.grpc.proto.WatchCancelRequest;
import com.tomgs.learning.grpc.proto.WatchCreateRequest;
import com.tomgs.learning.grpc.proto.WatchResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;

import java.util.*;
import java.util.concurrent.*;

/**
 * WatchManager
 *
 * @author tomgs
 * @since 1.0
 */
@Slf4j
public class WatchManager {

    public static final WatchManager INSTANCE = new WatchManager();

    private static final int MAX_WAIT_EVENT_TIME = 100;
    private final BlockingQueue<DataChangeEvent> events;

    //private final Set<ByteString> watchKeySet = new HashSet<>();

    private final static byte[] EMPTY_BYTE = new byte[0];

    private final static Map<byte[], byte[]> watchKeyMap = new TreeMap<>(BytesUtil.getDefaultByteArrayComparator());

    private static final Map<String, AbstractConnection<WatchResponse>> connections = new ConcurrentHashMap<>(16);

    private WatchManager() {
        this.events = new ArrayBlockingQueue<>(16);
    }

    public void notify(DataChangeEvent event) {
        try {
            events.put(event);
        } catch (InterruptedException e) {
            log.warn("There are too many events, this event {} will be ignored.", event.getType());
            // set the interrupted flag
            Thread.currentThread().interrupt();
        }
    }

    public DataChangeEvent watchEventPoll(int maxWaitEventTime, TimeUnit milliseconds) throws InterruptedException {
        return events.poll(maxWaitEventTime, milliseconds);
    }

    public boolean containsWatchKey(String key) {
        final ByteString watchKey = ByteString.copyFromUtf8(key);
        return watchKeyMap.containsKey(watchKey.toByteArray());
    }

    public boolean containsWatchKey(byte[] key) {
        return watchKeyMap.containsKey(key);
    }

    public void addWatchRequest(WatchCreateRequest createRequest) {
        final ByteString key = createRequest.getKey();
        watchKeyMap.put(key.toByteArray(), EMPTY_BYTE);
    }

    public void cancelWatchRequest(WatchCancelRequest cancelRequest) {
        final ByteString key = cancelRequest.getKey();
        watchKeyMap.remove(key.toByteArray());
    }

    public void addWatchConnection(String connectionId, AbstractConnection<WatchResponse> watchStreamConnection) {
        connections.put(connectionId, watchStreamConnection);
    }

    public boolean hasClientConnection() {
        return connections.size() != 0;
    }

    public void removeWatchConnection(String connectionId) {
        connections.remove(connectionId);
    }

    public Collection<AbstractConnection<WatchResponse>> getWatchConnections() {
        return connections.values();
    }

    public void startWatchEvent(String eventName) {
        final Consumer consumer = new Consumer(eventName);
        consumer.setDaemon(true);
        consumer.start();
    }

    private class Consumer extends Thread {

        Consumer(String name) {
            setName(name);
        }

        @Override
        @SuppressWarnings("InfiniteLoopStatement")
        public void run() {
            Future<Void> task = null;
            boolean hasNewEvent = false;
            DataChangeEvent lastEvent = null;
            while (true) {
                try {
                    // Today we only care about service event,
                    // so we simply ignore event until the last task has been completed.
                    DataChangeEvent event = watchEventPoll(MAX_WAIT_EVENT_TIME, TimeUnit.MILLISECONDS);
                    if (event != null) {
                        hasNewEvent = true;
                        lastEvent = event;
                    }
                    if (hasClientConnection() && needNewTask(hasNewEvent, task)) {
                        task = ThreadUtil.execAsync(new EventHandleTask(lastEvent));
                        hasNewEvent = false;
                        lastEvent = null;
                    }
                } catch (InterruptedException e) {
                    log.warn("Thread {} is be interrupted.", getName());
                    // set the interrupted flag
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private boolean needNewTask(boolean hasNewEvent, Future<Void> task) {
        return hasNewEvent && (task == null || task.isDone());
    }

    private class EventHandleTask implements Callable<Void> {

        private final DataChangeEvent event;

        EventHandleTask(DataChangeEvent event) {
            this.event = event;
        }

        @Override
        public Void call() throws Exception {
            handleEvent(event);
            return null;
        }
    }

    private void handleEvent(DataChangeEvent event) {
        if (!hasClientConnection()) {
            return;
        }

        log.info("watch: event {} trigger push.", event.getType());

        for (AbstractConnection<WatchResponse> connection : getWatchConnections()) {
            // WatchedStatus watchedStatus = connection.getWatchedStatusByType(SERVICE_ENTRY_COLLECTION);
            //if (watchedStatus != null) {
            //    connection.push(, watchedStatus);
            //}
            WatchResponse response = WatchResponse.newBuilder().setEvent(event).build();
            connection.push(response, null);
        }
    }
}
