package com.tomgs.springboot.webflux.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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

}
