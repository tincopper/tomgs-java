package com.tomgs.learning.okhttp;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.Objects;

/**
 * MockServer
 *
 * @author tomgs
 * @since 1.0
 */
public class BaseMockServerTest {

    private static MockWebServer server;

    @BeforeClass
    public static void initMockServer() throws IOException {
        server = new MockWebServer();
        final Dispatcher dispatcher = new Dispatcher() {

            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest request) {
                switch (Objects.requireNonNull(request.getPath())) {
                    case "/v1/login":
                        return new MockResponse().setResponseCode(200)
                                .setBody("{\"id\":4234, \"domain\":\"itcore\"}");
                    case "/v1/ping":
                        return new MockResponse().setResponseCode(200)
                                .setBody("pong");
                    case "users/1":
                        return new MockResponse().setResponseCode(200)
                                .setBody("tomgs");
                    default:
                        return new MockResponse().setResponseCode(404);
                }
            }
        };
        server.setDispatcher(dispatcher);
        server.start(8080);
    }

    @AfterClass
    public static void closeMockWebServer() throws IOException {
        if (server != null) {
            server.close();
        }
    }
}
