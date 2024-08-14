package com.learning.webflux.router;

import com.learning.webflux.handler.SimpleHandlerFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> route(SimpleHandlerFunction simpleHandlerFunction){

        return RouterFunctions
                .route(GET("/handlerfunction/flux").and(accept(MediaType.APPLICATION_JSON)), simpleHandlerFunction::flux)
                .andRoute(GET("/handlerfunction/mono").and(accept(MediaType.APPLICATION_JSON)), simpleHandlerFunction::mono);
    }
}
