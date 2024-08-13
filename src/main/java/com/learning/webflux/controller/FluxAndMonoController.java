package com.learning.webflux.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.stream.Stream;

//It will start a Netty server (Server for Non blocking communications)

@RestController
public class FluxAndMonoController {

    @GetMapping("/getFlux")
    public Flux<Integer> getFlux(){
//        Returning a Flux here
        return Flux.just(1,2,3,4)
                .delayElements(Duration.ofSeconds(1)) // This is like introducing sleep() in between
                .log();

    }

//    Below will return a stream, (values keep on coming, and slowly fetched)
//    In older versions MediaType.APPLICATION_STREAM_JSON_VALUE was used, which is now deprecated
//    In newer versions, MediaType.TEXT_EVENT_STREAM_VALUE

//    In browser, support get function is called twice, each will run with separate values (1,2,3...)
//    If we shut down the server, it will gracefully send cancel to all the clients
    @GetMapping(value = "/getFluxStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> getFlux_Stream(){
//        Returning a Flux here
        return Flux.just(1,2,3,4,5,6,7,8,9,10)
                .delayElements(Duration.ofSeconds(1)) // This is like introducing sleep() in between
                .log();

    }

    @GetMapping(value = "/getFluxInfiniteStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> getFlux_InfiniteStream(){
//        Returning a Flux here
        return Flux.interval(Duration.ofSeconds(1))
                .log();

    }

    @GetMapping(value = "/getFluxInfiniteStreamNew", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> getFluxInfiniteStreamNew() {
        return Flux.fromStream(Stream.iterate(1, n -> n + 1))
                .delayElements(Duration.ofSeconds(1));
    }

    @GetMapping(value = "/getMono")
    public Mono<Integer> getMono(){
//        Returning a Mono here
        return Mono.just(1)
                .log();

    }
}
