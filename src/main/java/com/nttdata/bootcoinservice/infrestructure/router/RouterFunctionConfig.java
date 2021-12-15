package com.nttdata.bootcoinservice.infrestructure.router;

import com.nttdata.bootcoinservice.infrestructure.rest.handler.BootcointHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterFunctionConfig {
    String uri = "api/v1/bootcoin";

    @Bean
    public RouterFunction<ServerResponse> routes(BootcointHandler bootcointHandler) {
        return route(GET(uri), bootcointHandler::getall)
                .andRoute(GET(uri.concat("/phone/{number}")), bootcointHandler::getIdPhone)
                .andRoute(GET(uri.concat("/{id}")), bootcointHandler::getOne)
                .andRoute(POST(uri), bootcointHandler::save)
                .andRoute(PUT(uri.concat("/{id}")), bootcointHandler::update)
                .andRoute(DELETE(uri.concat("/{id}")), bootcointHandler::delete);
                //.andRoute(GET(uri.concat("/getAllRedis")), walletHandler::getAllRedis);


    }

}
