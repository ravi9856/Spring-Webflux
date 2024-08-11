package com.learning.webflux.fluxandmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

//    Difference between Flux and Mono is Mono takes one value, and Flux can take multiple
    @Test
    public void fluxTest(){

        Flux<String> stringFlux = Flux.just("ABC","XYZ","DDD")
//                .concatWith(Flux.error(new RuntimeException("Custom Exception")))
                .concatWith(Flux.just("XXX")) // This line will be printed only if error is not there
                .log(); // This is printing all the logs, like onNext() methods

        // This is the subscriber for above Flux, with error handling
        stringFlux
                .subscribe(System.out::println,
                        e -> System.err.println("Exception: "+e),
                        () -> System.out.println("Flow Completed")); // onComplete will also be printed only if no error
    }

//    How to really test Flux
    @Test
    public void fluxRealTest_WithoutError(){

        Flux<String> stringFlux = Flux.just("ABC","XYZ","DDD")
                .log(); // This is printing all the logs, like onNext() methods

//        Below StepVerifier.create takes care of subscribing as well, so subscribe method like above, is not required
        StepVerifier.create(stringFlux)
                .expectNext("ABC")
                .expectNext("XYZ")
                .expectNext("DDD") // If any value doesn't match, or order doesn't match, test case will fail
                .verifyComplete(); // This is the main call, which starts subscribing, without this, process won't start

    }

//    All test cases can be executed with/without errors

    @Test
    public void fluxRealTest_WithError(){

        Flux<String> stringFlux = Flux.just("ABC","XYZ","DDD")
                .concatWith(Flux.error(new RuntimeException("Custom Exception")))
                .log(); // This is printing all the logs, like onNext() methods

    //        Below StepVerifier.create takes care of subscribing as well, so subscribe method like above, is not required
        StepVerifier.create(stringFlux)
                .expectNext("ABC")
                .expectNext("XYZ")
                .expectNext("DDD") // If any value doesn't match, or order doesn't match, test case will fail
    //                    .expectError(RuntimeException.class) // Instead of verifyComplete verify will be used
                .expectErrorMessage("Custom Exception") // Either this, or above can come. Both together are not possible
                .verify();
//                    .verifyComplete(); // This is the main call, which starts subscribing, without this, process won't start


    }

    @Test
    public void fluxRealTest_Count(){

        Flux<String> stringFlux = Flux.just("ABC","XYZ","DDD")
                .log(); // This is printing all the logs, like onNext() methods

//        Below StepVerifier.create takes care of subscribing as well, so subscribe method like above, is not required
        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .verifyComplete(); // This is the main call, which starts subscribing, without this, process won't start

    }

    @Test
    public void fluxRealTest_ElementOneLine(){

        Flux<String> stringFlux = Flux.just("ABC","XYZ","DDD")
                .log(); // This is printing all the logs, like onNext() methods

//        Below StepVerifier.create takes care of subscribing as well, so subscribe method like above, is not required
        StepVerifier.create(stringFlux)
                .expectNext("ABC","XYZ","DDD") // All in one line
                .verifyComplete(); // This is the main call, which starts subscribing, without this, process won't start

    }

    @Test
    public void monoTest(){

        Mono<String> stringMono = Mono.just("AAA");

        StepVerifier.create(stringMono.log()) // Log can come here also
                .expectNext("AAA")
                .verifyComplete();

    }

    @Test
    public void monoTest_WithError(){

        StepVerifier.create(Mono.error(new RuntimeException("Exception E1")).log()) //Error and log can be here
                .expectError(RuntimeException.class)
                .verify();

    }
}
