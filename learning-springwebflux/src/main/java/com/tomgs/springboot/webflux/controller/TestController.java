package com.tomgs.springboot.webflux.controller;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalTime;

/**
 * @author tomgs
 * @since 2021/1/27
 */
@RestController
public class TestController {

  @GetMapping("/hello")
  public Mono<String> hello() {
    return Mono.just("Hello world");
  }

  @GetMapping("/hello1")
  public String hello1() {
    return "Hello world1";
  }

  // https://www.baeldung.com/spring-server-sent-events
  @GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<String> streamFlux() {
    return Flux.interval(Duration.ofSeconds(1))
            .map(sequence -> "Flux - " + LocalTime.now().toString());
  }

  @GetMapping(path = "/stream-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent<String>> streamEvents() {
    return Flux.interval(Duration.ofSeconds(1))
            .map(sequence -> {
              if (sequence % 2 == 0) {
                return ServerSentEvent.<String> builder()
                        .id(String.valueOf(sequence))
                        .retry(Duration.ofSeconds(3))
                        .event("diyEventType")
                        .data("SSE - " + LocalTime.now().toString())
                        .build();
              }
              // default event
              return ServerSentEvent.<String> builder()
                      .id(String.valueOf(sequence))
                      .retry(Duration.ofSeconds(3))
                      .event("message")
                      .data("SSE - " + LocalTime.now().toString())
                      .build();
            });
  }

}
