package com.nttdata.bootcoinservice.infrestructure.rest.handler;

import com.nttdata.bootcoinservice.application.operations.BootcoinOperations;
import com.nttdata.bootcoinservice.domain.Bootcoin;
import com.nttdata.bootcoinservice.infrestructure.redis.BootcoinRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Slf4j
@Component
public class BootcointHandler {

    @Autowired
    private BootcoinOperations bootcoinOperations;

    @Autowired
    private Validator validator;

    @Autowired
    BootcoinRedisService bootcoinRedisService;

    public Mono<ServerResponse> getall(ServerRequest serverRequest) {
        //Wallet wallet = new Wallet("1", "DNI", "45236852", "975804256", "12SD2SD1S2DS2ER2WEWW", "wvera@gmail.com", 200.0, LocalDateTime.now());
        //return ServerResponse.ok().contentType(APPLICATION_JSON).body(Mono.just(wallet), Wallet.class);
        return ServerResponse.ok().contentType(APPLICATION_JSON)
                .body(bootcoinOperations.findAll(), Bootcoin.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getOne(ServerRequest serverRequest) {
        return bootcoinOperations.findById(serverRequest.pathVariable("id"))
                .flatMap(wallet -> {
                            bootcoinRedisService.save(wallet); //Save in redis
                    return ServerResponse
                                    .ok().contentType(APPLICATION_JSON)
                                    .body(fromValue(wallet))
                                    .switchIfEmpty(ServerResponse.notFound().build());
                        }
                );
    }

    //Get redis
    public Mono<ServerResponse> getAllRedis(ServerRequest serverRequest) {
        System.out.println("wallet GetAll");
        return ServerResponse.ok().contentType(APPLICATION_JSON)
                .body(bootcoinRedisService.getAll(), Bootcoin.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getIdPhone(ServerRequest serverRequest) {
        return bootcoinOperations.findByNumberPhone(serverRequest.pathVariable("number"))
                .flatMap(wallet -> ServerResponse
                        .ok().contentType(APPLICATION_JSON)
                        .body(fromValue(wallet))
                        .switchIfEmpty(ServerResponse.notFound().build())
                );
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        log.info("Req en save - wallet");
        Mono<Bootcoin> walletMono = serverRequest.bodyToMono(Bootcoin.class);

        return walletMono.flatMap(wallet -> {
            Errors errors = new BeanPropertyBindingResult(wallet, Bootcoin.class.getName());
            validator.validate(wallet, errors);

            if (errors.hasErrors()) {
                return Flux.fromIterable(errors.getFieldErrors())
                        .map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                        .collectList()
                        .flatMap(listString -> ServerResponse.badRequest().body(fromValue(listString)));
            } else {
                return bootcoinOperations.save(wallet)
                        .flatMap(walletdb -> ServerResponse
                                .status(HttpStatus.CREATED)
                                .contentType(APPLICATION_JSON)
                                .body(fromValue(walletdb)));
            }
        });

    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        Mono<Bootcoin> walletMono = serverRequest.bodyToMono(Bootcoin.class);
        String id = serverRequest.pathVariable("id");
        return walletMono.flatMap(wallet -> bootcoinOperations.update(id, wallet))
                .flatMap(walletCreated -> ServerResponse.created(URI.create("/api/v1/wallet/".concat(walletCreated.getId())))
                        .contentType(APPLICATION_JSON)
                        .body(fromValue(walletCreated))
                ).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return bootcoinOperations.findById(id)
                .flatMap(wallet -> bootcoinOperations.delete(id).then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }


}
