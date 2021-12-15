package com.nttdata.bootcoinservice.application.model;

import com.nttdata.bootcoinservice.domain.Bootcoin;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BootcoinRepository {

    Flux<Bootcoin> findAll();

    Mono<Bootcoin> findById(String id);

    Mono<Bootcoin> save(Bootcoin bootcoin);

    Mono<Bootcoin> update(String id, Bootcoin bootcoin);

    Mono<Void> delete(String id);

    Mono<Bootcoin> findByNumberPhone(String numberPhone);

}
