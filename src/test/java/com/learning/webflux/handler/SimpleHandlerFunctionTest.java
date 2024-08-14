package com.learning.webflux.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest // Because @WebfluxTest only scans @RestController, and handler function is a @Component
@AutoConfigureWebTestClient
public class SimpleHandlerFunctionTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void fluxApproach1(){

        Flux<Integer> integerFlux = webTestClient
                .get()
                .uri("/handlerfunction/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange() // This will actually connect to the endpoint
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integerFlux)
                .expectSubscription() // Subscription sent to us via Network
                .expectNext(1,2,3,4 )
                .verifyComplete();
    }

    @Test
    public void monoTest(){

        Integer expected = 1;

        webTestClient
                .get()
                .uri("/handlerfunction/mono")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith((response) -> {
                    assertEquals(expected, response.getResponseBody());
                });

    }
}
