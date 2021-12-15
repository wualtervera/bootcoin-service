package com.nttdata.bootcoinservice.application.impl;

import com.nttdata.bootcoinservice.application.model.BootcoinRepository;
import com.nttdata.bootcoinservice.application.operations.BootcoinOperations;
import com.nttdata.bootcoinservice.domain.Bootcoin;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BootcoinOperationsImpl implements BootcoinOperations {

    @Autowired
    private BootcoinRepository bootcoinRepository;

    @Override
    public Flux<Bootcoin> findAll() {
        return bootcoinRepository.findAll();
    }

    @Override
    public Mono<Bootcoin> findById(String id) {
        return bootcoinRepository.findById(id);
    }

    @Override
    public Mono<Bootcoin> save(Bootcoin bootcoin) {
        return bootcoinRepository.save(bootcoin);
    }

    @Override
    public Mono<Bootcoin> update(String id, Bootcoin bootcoin) {
        return bootcoinRepository.update(id, bootcoin);
    }

    @Override
    public Mono<Void> delete(String id) {
        return bootcoinRepository.delete(id);
    }

    @Override
    public Mono<Bootcoin> findByNumberPhone(String numberPhone) {
        return bootcoinRepository.findByNumberPhone(numberPhone);
    }

}
