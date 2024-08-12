package com.learning.webflux.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebFluxTest // Scans all classes with @RestController tag
public class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void fluxApproach1(){

        Flux<Integer> integerFlux = webTestClient
                .get()
                .uri("/getFlux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integerFlux)
                .expectSubscription() // Subscription sent to us via Network
                .expectNext(1,2,3,4 )
                .verifyComplete();
    }

    @Test
    public void fluxApproach2(){

        webTestClient.get()
                .uri("/getFlux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class) // Instead of returning, expecting here
//                .returnResult(Integer.class)
                .hasSize(4);

    }

    @Test
    public void fluxApproach3(){

        List<Integer> expectedList = Arrays.asList(1,2,3,4);
        EntityExchangeResult<List<Integer>> resultList = webTestClient.get()
                .uri("/getFlux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();

        assertEquals(expectedList, resultList.getResponseBody());

    }

    @Test
    public void fluxApproach4(){

        List<Integer> expectedList = Arrays.asList(1,2,3,4);
        EntityExchangeResult<List<Integer>> resultList = webTestClient.get()
                .uri("/getFlux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith((response) -> {
                    assertEquals(expectedList, response.getResponseBody());
                })
                .returnResult();

    }

    @Test
    public void fluxApproachStreamValues(){

        Flux<Long> longFlux = webTestClient
                .get()
                .uri("/getFluxInfiniteStream")
                .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE)) // Change here
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(longFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .thenCancel() // Cancel after these values
                .verify();

    }
}
